package Notificaciones;

import Reserva.FormularioController;
import Reserva.Insert;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javaapplication1.JavaApplication1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import tabla.tablaController;

public class NotificacionesController {

    // Variables de instancia para almacenar información del usuario y notificaciones
    private int folio;
    private int usuario;                              // ID del usuario actual (no utilizado en este fragmento)
    private int trabajador;                           // ID del trabajador actual
    private int folioReserva;
    private String estado1;
    private int id_Horario=0;
    private int id_Mobiliario=0;
    private int horaInicio;
    private String hora_Inicio; // Hora de inicio de la reserva
    private String hora_Fin; // Hora de fin de la reserva
    private String texto;                             // Texto que determina el tipo de notificación a cargar
    private int id_selected = 0; // ID del mobiliario seleccionado en la tabla
    private String estado=null; 
    private String textoArea;
    private int trabajador_Select;
    private  LocalDate fechaCalendario;
    private List<Integer> cadenaUsuarios = new ArrayList<>(); // Lista de IDs de trabajadores
    private List<Integer> cadenaFecha = new ArrayList<>(); 
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
    @FXML
    private TextField id_select;
    @FXML
    private TextField id_select1;
        @FXML
    private TextField id_mobiliario;
    @FXML
    private Pane paneFolioReserva;
    @FXML
    private Pane paneEditar;
    @FXML
    private Pane paneFondo;
    @FXML
    private Button editar;

    @FXML
    private Button eliminar;
    @FXML
    private Button btn_Rechazar;
    @FXML
    private Button Aprobar;
    
    @FXML
    private TextArea noDisponible;
     @FXML
    private TextArea comentario;
    @FXML
    private DatePicker calendario1;
    

    @FXML
    private MenuButton menuRazon;

    @FXML
    private MenuButton menu_hhInicio;

    @FXML
    private MenuButton menu_hhTermino;

    @FXML
    private MenuButton menu_mmInicio;

    @FXML
    private MenuButton menu_mmTermino;

    @FXML
    private AnchorPane panefolioReserva;

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

        switch (texto) {
            case "Todos":
               for (int i = 1; i < cadenaUsuarios.size(); i++) {
                    trabajador = cadenaUsuarios.get(i);  // Obtener el ID del trabajador actual.
                    cargarDatosNotificaciones(query, trabajador);  // Cargar datos de notificaciones.
                }
                break;

            case "Por validar":
                query = queryBase + " AND Estado = 'Por validar'"; // Consulta específica para "Por validar".
                for (int i = 1; i < cadenaUsuarios.size(); i++) { // Evita el índice 0.
                    trabajador = cadenaUsuarios.get(i);
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Validado":
                query = queryBase + " AND Estado = 'Validado'"; // Consulta específica para "Validado".
                for (int i = 1; i < cadenaUsuarios.size(); i++) { // Evita el índice 0.
                    trabajador = cadenaUsuarios.get(i);
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Rechazado":
                query = queryBase + " AND Estado = 'Rechazado'"; // Consulta específica para "Rechazado".
                for (int i = 1; i < cadenaUsuarios.size(); i++) { // Evita el índice 0.
                    trabajador = cadenaUsuarios.get(i);
                    cargarDatosNotificaciones(query, trabajador);
                }
                break;

            case "Mis reservas":
                if (!cadenaUsuarios.isEmpty()) {
                    trabajador = cadenaUsuarios.get(0); // Toma solo el primer usuario.
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
     * @param trabajador El ID del trabajador cuyas notificaciones se van a
     * cargar.
     */
    private void cargarDatosNotificaciones(String query, int trabajador) {
        try (PreparedStatement pst = con.prepareStatement(query)) {
             if (trabajador != -1) { // Solo establece el parámetro si el trabajador es específico.
            pst.setInt(1, trabajador);
            }
            try (ResultSet rs = pst.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Set<Integer> idsExistentes = detalleReserva.stream()
                        .map(ContenidoNotificacion::getId_Notificacion) // Extrae los IDs existentes.
                        .collect(Collectors.toSet());

                while (rs.next()) {
                    int idNotificacion = rs.getInt("id_detalleReserva");
                    if (!idsExistentes.contains(idNotificacion)) { // Evita duplicados.
                        String fecha = sdf.format(rs.getTimestamp("Fecha"));
                        String descripcion = rs.getString("RazondeReserva");
                        String estadoReserva = rs.getString("Estado");

                        detalleReserva.add(new ContenidoNotificacion(
                                idNotificacion, trabajador, fecha, descripcion, estadoReserva
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());
        }

        tablaDetalleReserva.setItems(detalleReserva);
        tablaDetalleReserva.refresh();
    }

    /**
     * Método que establece el usuario y carga sus notificaciones
     * correspondientes.
     *
     * @param cadenaUsuarios Lista de IDs de los usuarios cuyas notificaciones
     * se van a cargar.
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

        // Listener que actualiza el campo id_select cuando se selecciona un nuevo item en la tabla.
        tablaDetalleReserva.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id_selected = newValue.getId_Notificacion(); // Obtiene el ID del mobiliario seleccionado.
                estado = newValue.getEstado(); // Obtiene el ID del mobiliario seleccionado.
                id_select.setText("" + id_selected); // Muestra el ID en el campo correspondiente.
                trabajador_Select=newValue.getId_trabajador();
                 id_select1.setText("" + id_selected); // Muestra el ID en el campo correspondiente.
                folio = Integer.parseInt(id_select.getText());
                if("Por validar".equals(estado)&&trabajador_Select!=cadenaUsuarios.get(0)){
                    
                    editar.setVisible(false);
                    eliminar.setVisible(false);
                    Aprobar.setVisible(true);
                    comentario.setVisible(true);
                    btn_Rechazar.setVisible(true);
                }
             

             try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reserva/Formulario.fxml"));  // Cargar el FXML correspondiente a la tabla
            Parent root = loader.load();

            // Obtener el controlador de `tablaController`
            FormularioController formulario = loader.getController();
            formulario.folioReserva(folio);  // Enviar el nombre del botón y el ID del usuario

            // Limpiar el contenido de `tabla` y añadir el contenido de `table.fxml`
            panefolioReserva.getChildren().clear();
            panefolioReserva.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();  // Manejo básico de errores en caso de fallos al cargar el FXML.
        }
                
            }
        });
         calendario1.setOnAction(event -> {
            LocalDate fechaSeleccionada = calendario1.getValue();
             
            FechaSeleccionada(fechaSeleccionada);
        });
        
                    menuRazon.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                // Obtén el texto del MenuItem seleccionado
                String selectedText = menuItem.getText();
                // Opcional: Actualiza el texto del MenuButton
                menuRazon.setText(selectedText);
            });
        });
        
        
             menu_hhInicio.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                // Obtén el texto del MenuItem seleccionado
                String selectedText = menuItem.getText();
                // Opcional: Actualiza el texto del MenuButton
                menu_hhInicio.setText(selectedText);
            });
        });
            menu_mmInicio.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                // Obtén el texto del MenuItem seleccionado
                String selectedText = menuItem.getText();
                // Opcional: Actualiza el texto del MenuButton
                menu_mmInicio.setText(selectedText);
            });
        });
             menu_hhTermino.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                // Obtén el texto del MenuItem seleccionado
                String selectedText = menuItem.getText();
                // Opcional: Actualiza el texto del MenuButton
                menu_hhTermino.setText(selectedText);
            });
        });
             menu_mmTermino.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                // Obtén el texto del MenuItem seleccionado
                String selectedText = menuItem.getText();
                // Opcional: Actualiza el texto del MenuButton
                menu_mmTermino.setText(selectedText);
            });
        });
    }

    @FXML
    public void folioReserva() {
        //Metodo que recibe id_Selected y que nos permite cargar un folio Reserva
    }

    @FXML
    void btn_Editar(ActionEvent event) {
        paneEditar.setVisible(true);
        panefolioReserva.getChildren().clear(); // Limpia el contenedor
        panefolioReserva.getChildren().add(paneEditar); // Agrega el panel existente

        String query = "SELECT * FROM detalle_reserva WHERE id_detalleReserva = ?";
        LocalDate fechaLocalDate = null;
        String[] divInicio = null;
        String horaTermino = null;
        String[] divTermino = null;
        String razon = null;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, folio);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    id_Horario = rs.getInt("id_Horario");
                    id_Mobiliario = rs.getInt("id_Mobiliario");
                    razon = rs.getString("RazondeReserva");
                }
                id_mobiliario.setText(Integer.toString(id_Mobiliario));
                menuRazon.setText(razon);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las ediciones detallereserva: " + e.getMessage());
        }


        query = "SELECT * FROM horario WHERE id_Horario = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id_Horario);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String hora = rs.getString("Hora_inicio");
                    divInicio = hora.split(":");
                    horaTermino = rs.getString("Hora_Termino");
                    divTermino = horaTermino.split(":");
                }
                menu_hhInicio.setText(divInicio[0]);
                menu_mmInicio.setText(divInicio[1]);
                menu_hhTermino.setText(divTermino[0]);
                menu_mmTermino.setText(divTermino[1]);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las ediciones horario: " + e.getMessage());
        }
    }

    @FXML
void btn_Eliminar(ActionEvent event) {
    Alert alerta;
    try {
        // Validar estado antes de proceder
        if (estado == null || !estado.equals("Validado")) {
            // Obtener el ID de la reserva desde el campo de texto
            int folioReserva = Integer.parseInt(id_select.getText());
            int id_Horario = -1;

            // Obtener id_Horario asociado al detalle_reserva
            String query_Horario = "SELECT id_Horario FROM detalle_reserva WHERE id_detalleReserva = ?";
            try (PreparedStatement pst_Horario = con.prepareStatement(query_Horario)) {
                pst_Horario.setInt(1, folioReserva);  
                try (ResultSet rs_Horario = pst_Horario.executeQuery()) {
                    if (rs_Horario.next()) {  
                        id_Horario = rs_Horario.getInt("id_Horario");
                    }
                }
            }

            if (id_Horario == -1) {
                alerta = new Alert(Alert.AlertType.ERROR, "No se encontró el id_Horario asociado a este detalle_reserva.");
                alerta.showAndWait();
                return;
            }

            // Eliminar detalle_reserva
            String queryDetalleReserva = "DELETE FROM detalle_reserva WHERE id_detalleReserva = ?";
            try (PreparedStatement pstDetalleReserva = con.prepareStatement(queryDetalleReserva)) {
                pstDetalleReserva.setInt(1, folioReserva);
                int affectedRowsDetalle = pstDetalleReserva.executeUpdate();

                if (affectedRowsDetalle > 0) {
                    alerta = new Alert(Alert.AlertType.INFORMATION, "Detalle de reserva eliminado exitosamente.");
                    alerta.showAndWait();
                } else {
                    alerta = new Alert(Alert.AlertType.WARNING, "No se encontró ningún detalle_reserva con ese ID.");
                    alerta.showAndWait();
                }
            }

            // Eliminar horario asociado
            String queryHorario = "DELETE FROM horario WHERE id_Horario = ?";
            try (PreparedStatement pstHorario = con.prepareStatement(queryHorario)) {
                pstHorario.setInt(1, id_Horario);
                int affectedRowsHorario = pstHorario.executeUpdate();

                if (affectedRowsHorario > 0) {
                    alerta = new Alert(Alert.AlertType.INFORMATION, "Horario eliminado exitosamente.");
                    alerta.showAndWait();
                } else {
                    alerta = new Alert(Alert.AlertType.WARNING, "No se encontró ningún horario con ese ID.");
                    alerta.showAndWait();
                }
            }
        } else {
            alerta = new Alert(Alert.AlertType.WARNING, "El folio ya fue validado y no puede eliminarse.");
            alerta.showAndWait();
        }
    } catch (SQLException e) {
        alerta = new Alert(Alert.AlertType.ERROR, "Error al eliminar la reserva: " + e.getMessage());
        alerta.showAndWait();
    } catch (NumberFormatException e) {
        alerta = new Alert(Alert.AlertType.ERROR, "El ID ingresado no es un número válido.");
        alerta.showAndWait();
    }
}

     @FXML
    void btn_Rechazar(ActionEvent event) {
        String comentarios=comentario.getText();
        Insert aprobar= new Insert();
        aprobar.Rechazar(comentarios, id_selected);
        
    }
        @FXML
    void btnModificarReserva(ActionEvent event) {

         fechaCalendario = calendario1.getValue(); // Obtener la fecha seleccionada desde el DatePicker
      
        String hora_Inicio = menu_hhInicio.getText()+":"+menu_mmInicio.getText(); // Obtener hora de inicio desde el campo correspondiente.
        String hora_Fin = menu_hhTermino.getText()+":"+menu_mmTermino.getText();   // Obtener hora de fin desde el campo correspondiente.
        String razon= menuRazon.getText();
        if (fechaCalendario!= null && hora_Inicio != null && hora_Fin != null && id_selected != 0) {  // Verificar que se ha seleccionado una fecha válida.
          String  dia = fechaCalendario.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
          String  fecha_Seleccionada = fechaCalendario.toString();
           boolean validarHorario = FechaSeleccionada();
                if (validarHorario) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Horario Ocupado");
                } else {
                     Insert dato = new Insert();
            dato.editarHorario(id_Horario, id_selected,hora_Inicio, hora_Fin, dia, fechaCalendario,  razon);
                }
           

            JOptionPane.showInternalMessageDialog(null, "Se ha modificado su reserva");  // Mostrar un diálogo interno con mensaje.

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Se ha modificado su reserva");  // Crear una alerta confirmando que se ha creado la reserva.
            alerta.showAndWait();  // Mostrar la alerta y esperar a que sea cerrada por el usuario.
        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Faltan datos por llenar."); 
            
            // Mensaje en consola si no hay fecha seleccionada.
        }

    }
    
       private boolean FechaSeleccionada() {
        LocalDate fecha = calendario1.getValue();
        try {
            String[] divInicio = null;
            String horaTermino = null;
            String[] divTermino = null;
            String cadFecha;
            Integer cad;
            cadenaFecha = null;
            if (con != null) { // Verificar si se ha establecido una conexión.
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM detalleReserva WHERE id_Mobiliario = '" + id_selected + "'");

                while (rs.next()) {
                    int idMobiliario = rs.getInt("id_Horario");
                    cadenaFecha.add(idMobiliario);
                }
                if (cadenaFecha != null) {
                    
                    for (int i = 0; i < cadenaFecha.size(); i++) {
                        ResultSet rsFecha = st.executeQuery("SELECT * FROM horario WHERE id_Horario = '" + cadenaFecha.get(i)+ "'");
                        while (rsFecha.next()) {
                            String hora = rsFecha.getString("Hora_inicio");
                            horaTermino = rsFecha.getString("Hora_Termino");
                            
                            if(validarHora(hora,horaTermino)){
                                return true;
                            }
                        }
                        rsFecha.close();
                    }
                }
                st.close();
                rs.close(); // Cerrar el ResultSet después de usarlo.
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage()); // Manejo básico de errores en caso de fallos en la consulta.
        }
        return false;
    }
    
    public boolean validarHora(String hora,String horaTermino){
         LocalTime inicioExistenteTime = LocalTime.parse(hora);
    LocalTime terminoExistenteTime = LocalTime.parse(horaTermino);
    LocalTime inicioNuevoTime = LocalTime.parse(hora_Inicio);
    LocalTime terminoNuevoTime = LocalTime.parse(hora_Fin);
    return (inicioNuevoTime.isBefore(terminoExistenteTime) && terminoNuevoTime.isAfter(inicioExistenteTime));
    }

    private void FechaSeleccionada(LocalDate fechaSeleccionada) {
         textoArea=null;
         noDisponible.clear();
         String agregar;
         noDisponible.setText("Horarios no disponibles en la fecha:"+ fechaSeleccionada);
         textoArea= noDisponible.getText();
        try {
            String[] divInicio = null;
            String horaTermino = null;
            String[] divTermino = null;
            String cadFecha;
            Integer cad;
            cadenaFecha = null;
            if (con != null) { // Verificar si se ha establecido una conexión.
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM detalleReserva WHERE id_Mobiliario = '" + id_selected + "'");

                while (rs.next()) {
                    int idMobiliario = rs.getInt("id_Horario");
                    cadenaFecha.add(idMobiliario);
                }
                if (cadenaFecha != null) {
                    noDisponible.setVisible(true);
                    for (int i = 0; i < cadenaFecha.size(); i++) {
                        ResultSet rsFecha = st.executeQuery("SELECT * FROM horario WHERE id_Horario = '" + cadenaFecha.get(i)+ "'");
                        while (rsFecha.next()) {
                            String hora = rsFecha.getString("Hora_inicio");
                            horaTermino = rsFecha.getString("Hora_Termino");
                            agregar=hora+" - "+horaTermino;
                            textoArea=textoArea+agregar;
                           noDisponible.setText("\n"+textoArea);
                        }
                        rsFecha.close();
                    }
                }
                st.close();
                rs.close(); // Cerrar el ResultSet después de usarlo.
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage()); // Manejo básico de errores en caso de fallos en la consulta.
        }
    }
    @FXML
    private void Aprobar(ActionEvent event){
        String comentarios=comentario.getText();
        Insert aprobar= new Insert();
        aprobar.crearReserva(folio, comentarios, cadenaUsuarios.get(0));
    }

}
