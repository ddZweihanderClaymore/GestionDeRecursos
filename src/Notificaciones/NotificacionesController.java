package Notificaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.JavaApplication1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NotificacionesController {

    // Variables de instancia para almacenar información del usuario y notificaciones
    private int usuario;                              // ID del usuario actual (no utilizado en este fragmento)
    private int trabajador;                           // ID del trabajador actual
    private String texto;                             // Texto que determina el tipo de notificación a cargar
    private List<Integer> cadenaUsuarios = new ArrayList<>(); // Lista de IDs de trabajadores
    private Connection con = JavaApplication1.getConnection(); // Conexión a la base de datos
    private ObservableList<ContenidoNotificacion> detalleReserva = FXCollections.observableArrayList(); // Lista observable para la tabla

    // Elementos de la interfaz de usuario
    @FXML
    private TableColumn<ContenidoNotificacion, String> colDescripcion;   // Columna para la descripción de la notificación
    @FXML
    private TableColumn<ContenidoNotificacion, Integer> colId_Trabajador; // Columna para el ID del trabajador
    @FXML
    private TableColumn<ContenidoNotificacion, String> colEstado;         // Columna para el estado de la notificación
    @FXML
    private TableColumn<ContenidoNotificacion, String> colFecha;          // Columna para la fecha de la notificación
    @FXML
    private TableColumn<ContenidoNotificacion, Integer> colId_Notificacion; // Columna para el ID de la notificación
    @FXML
    private TableView<ContenidoNotificacion> tablaDetalleReserva;        // Tabla para mostrar las notificaciones

    public NotificacionesController() {
        // Constructor vacío
    }

    /**
     * Método que carga las notificaciones en función del estado y los usuarios.
     */
    private void cargarNotificaciones() {
        detalleReserva.clear(); // Limpia la lista antes de cargar nuevas notificaciones.
        
        String queryBase = "SELECT * FROM detalle_reserva WHERE id_trabajador = ?";
        String query = queryBase;

        System.out.println("Texto recibido: " + texto); // Diagnóstico del caso recibido.

        switch (texto) {
            case "Todos":
                query = queryBase;  // Consulta base para todos los trabajadores.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);  // Obtener el ID del trabajador actual.
                    System.out.println("Procesando trabajador (Todos): " + trabajador); // Diagnóstico.
                    cargarDatosNotificaciones(query, trabajador);  // Cargar datos de notificaciones.
                }
                break;

            case "Por validar":
                query = queryBase + " AND Estado = 'Por validar'";  // Consulta para notificaciones por validar.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);
                    System.out.println("Procesando trabajador (" + texto + "): " + trabajador); // Diagnóstico.
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Validado":
                query = queryBase + " AND Estado = 'Validado'";  // Consulta para notificaciones validadas.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);
                    System.out.println("Procesando trabajador (Validado): " + trabajador);
                    System.out.println("Consulta generada: " + query);
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Rechazado":
                query = queryBase + " AND Estado = 'Rechazado'";  // Consulta para notificaciones rechazadas.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);
                    System.out.println("Procesando trabajador (" + texto + "): " + trabajador); // Diagnóstico.
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Mis reservas":
                if (!cadenaUsuarios.isEmpty()) {
                    trabajador = cadenaUsuarios.get(0); // Solo toma el primer usuario.
                    System.out.println("Procesando trabajador (Mis reservas): " + trabajador); // Diagnóstico.
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;
        }

        tablaDetalleReserva.setItems(detalleReserva); // Actualiza la tabla con los datos cargados.
    }

    /**
     * Método que carga los datos de las notificaciones desde la base de datos.
     *
     * @param query La consulta SQL a ejecutar.
     * @param trabajador El ID del trabajador cuyas notificaciones se van a cargar.
     */
    private void cargarDatosNotificaciones(String query, int trabajador) {
        System.out.println("Procesando trabajador: " + trabajador);
        
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, trabajador);  // Establecer el ID del trabajador en la consulta.
            System.out.println("Ejecutando consulta: " + pst.toString());

            try (ResultSet rs = pst.executeQuery()) {
                boolean hasResults = false;  // Bandera para verificar si hay resultados en la consulta.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // Formato de fecha.

                while (rs.next()) {  // Iterar sobre los resultados de la consulta.
                    hasResults = true;
                    
                    int idNotificacion = rs.getInt("id_detalleReserva");  // Obtener el ID de la notificación.
                    java.sql.Timestamp fechaTimestamp = rs.getTimestamp("Fecha");  // Obtener la fecha como Timestamp.
                    String fecha = sdf.format(fechaTimestamp);  // Formatear la fecha a String.
                    String descripcion = rs.getString("RazondeReserva");  // Obtener la descripción de la reserva.
                    String estadoReserva = rs.getString("Estado");  // Obtener el estado de la reserva.

                    System.out.println("Valores a añadir: ID=" + idNotificacion + ", Trabajador=" + trabajador +
                                       ", fecha:" + fecha + ", descripcion:" + descripcion + ", estado: " + estadoReserva);

                    detalleReserva.add(new ContenidoNotificacion(idNotificacion, trabajador, fecha, descripcion, estadoReserva));  // Añadir a la lista observable.
                }

                if (!hasResults) {
                    System.out.println("No se encontraron resultados para el trabajador: " + trabajador);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());  // Manejo básico de errores en caso de fallos en la consulta.
        }

        tablaDetalleReserva.setItems(detalleReserva);  // Actualizar los elementos en la tabla con los datos cargados.
        tablaDetalleReserva.refresh();  // Asegura que se sincronice con los cambios en los datos.
    }

    /**
     * Método que establece el usuario y carga sus notificaciones correspondientes.
     *
     * @param cadenaUsuarios Lista de IDs de los usuarios cuyas notificaciones se van a cargar.
     * @param texto Texto que determina el tipo de notificación a cargar.
     */
    public void setUsuario(List<Integer> cadenaUsuarios, String texto) {
        this.cadenaUsuarios = cadenaUsuarios;  // Establecer lista de usuarios
        this.texto = texto;                   // Establecer tipo de notificación
        cargarNotificaciones();               // Cargar notificaciones una vez se establece el usuario
    }

    /**
     * Método que inicializa las columnas de la tabla al inicio del controlador.
     */
    @FXML
    public void initialize() {
        colId_Notificacion.setCellValueFactory(cellData -> cellData.getValue().Id_NotificacionProperty().asObject());
        colId_Trabajador.setCellValueFactory(cellData -> cellData.getValue().Id_trabajadorProperty().asObject());
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().DescripcionProperty());
        colFecha.setCellValueFactory(cellData -> cellData.getValue().FechaProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().EstadoProperty());
    }
}