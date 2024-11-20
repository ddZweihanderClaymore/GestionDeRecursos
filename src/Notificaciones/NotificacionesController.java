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

    private int usuario;
    private int trabajador;
    private String texto;
    private List<Integer> cadenaUsuarios = new ArrayList<>();
    private Connection con = JavaApplication1.getConnection();
    private ObservableList<ContenidoNotificacion> detalleReserva = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ContenidoNotificacion, String> colDescripcion;
    @FXML
    private TableColumn<ContenidoNotificacion, Integer> colId_Trabajador;
    @FXML
    private TableColumn<ContenidoNotificacion, String> colEstado;
    @FXML
    private TableColumn<ContenidoNotificacion, String> colFecha;
    @FXML
    private TableColumn<ContenidoNotificacion, Integer> colId_Notificacion;
    @FXML
    private TableView<ContenidoNotificacion> tablaDetalleReserva;

    public NotificacionesController() {
        // Constructor vacío
    }

    private void cargarNotificaciones() {
        detalleReserva.clear();
        String queryBase = "SELECT * FROM detalle_reserva WHERE id_trabajador = ?";
        String query = queryBase;

        switch (texto) {
            case "Todos":
                query = queryBase; // Consulta sin filtro de estado.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);
                    cargarDatosNotificaciones(query, trabajador, null);
                }
                break;

            case "Por validar":
            case "Validado":
            case "Rechazado":
                query = queryBase + " AND Estado = ?"; // Consulta con filtro de estado.
                for (int i = 0; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);
                    cargarDatosNotificaciones(query, trabajador, texto);
                }
                break;

            case "Mis reservas":
                if (!cadenaUsuarios.isEmpty()) {
                    trabajador = cadenaUsuarios.get(0); // Solo toma el primer usuario.
                    cargarDatosNotificaciones(query, trabajador, null);
                }
                break;
        }

        tablaDetalleReserva.setItems(detalleReserva); // Actualiza la tabla.
    }

    private void cargarDatosNotificaciones(String query, int trabajador, String estado) {
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, trabajador);

            if (estado != null) {
                pst.setString(2, estado);
            }

            try (ResultSet rs = pst.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                while (rs.next()) {
                    int idNotificacion = rs.getInt("id_detalleReserva");
                    java.sql.Timestamp fechaTimestamp = rs.getTimestamp("Fecha");
                    String fecha = sdf.format(fechaTimestamp);
                    String descripcion = rs.getString("RazondeReserva");
                    String estadoReserva = rs.getString("Estado");

                    detalleReserva.add(new ContenidoNotificacion(idNotificacion, trabajador, fecha, descripcion, estadoReserva));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());
        }
    }

    public void setUsuario(List<Integer> cadenaUsuarios, String texto) {
        this.cadenaUsuarios = cadenaUsuarios;
        this.texto = texto;
        cargarNotificaciones(); // Cargar notificaciones una vez se establece el usuario
    }

    @FXML
    public void initialize() {
        colId_Notificacion.setCellValueFactory(cellData -> cellData.getValue().Id_NotificacionProperty().asObject());
        colId_Trabajador.setCellValueFactory(cellData -> cellData.getValue().Id_trabajadorProperty().asObject());
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().DescripcionProperty());
        colFecha.setCellValueFactory(cellData -> cellData.getValue().FechaProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().EstadoProperty());
    }
}
