package tabla; // Paquete que contiene la clase controlador para la tabla

import javafx.collections.FXCollections; // Importa clases necesarias para manejar colecciones de JavaFX
import javafx.collections.ObservableList;
import javafx.fxml.FXML; // Importa la anotación FXML para vincular elementos de la interfaz
import javafx.scene.control.TableColumn; // Importa clases para manejar columnas de la tabla
import javafx.scene.control.TableView; // Importa clases para manejar la vista de la tabla
import javafx.scene.control.cell.PropertyValueFactory; // Importa clases para vincular propiedades a las celdas

import java.sql.Connection; // Importa clases para manejar conexiones a bases de datos
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate; // Importa clases para manejar fechas
import java.time.format.TextStyle;
import java.util.Locale; // Importa clases para manejar configuraciones locales
import javaapplication1.JavaApplication1; // Importa la clase principal que maneja la conexión a la base de datos
import javafx.event.ActionEvent; // Importa clases para manejar eventos
import javafx.scene.control.Alert; // Importa clases para mostrar alertas
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker; // Importa clases para seleccionar fechas
import javafx.scene.control.TextField;
import Reserva.Insert;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import javax.swing.JOptionPane; // Importa JOptionPane para mostrar diálogos

public class tablaController {

    private String fileName; // Nombre del archivo o subtipo de mobiliario seleccionado
    private int usuario;
    private int id_selected = 0; // ID del mobiliario seleccionado en la tabla
    private String hora_Inicio; // Hora de inicio de la reserva
    private String hora_Fin; // Hora de fin de la reserva
    private String dia; // Día de la semana seleccionado
    private String fecha_Seleccionada; // Fecha seleccionada
    private String razon;
    private String textoArea;

    @FXML
    private TableView<tablaContenido> tablaMobiliario; // Tabla que muestra el contenido del mobiliario

    @FXML
    private TableColumn<tablaContenido, Integer> colIdMobiliario; // Columna para el ID del mobiliario
    @FXML
    private TableColumn<tablaContenido, String> colNombre; // Columna para el nombre del mobiliario
    @FXML
    private TableColumn<tablaContenido, String> colDescripcion; // Columna para la descripción del mobiliario
    @FXML
    private TableColumn<tablaContenido, String> colStock; // Columna para el stock disponible

    @FXML
    private DatePicker calendario1; // Selector de fechas para elegir una fecha específica
    @FXML
    private TextArea noDisponible;
    @FXML
    private TextField id_select; // Campo de texto que muestra el ID seleccionado

    private ObservableList<tablaContenido> listaMobiliario = FXCollections.observableArrayList(); // Lista observable que contiene los datos del mobiliario

    @FXML
    private MenuButton menuRazon;
    @FXML
    private MenuButton menu_hhInicio;
    @FXML
    private MenuButton menu_mmInicio;
    @FXML
    private MenuButton menu_hhTermino;
    @FXML
    private MenuButton menu_mmTermino;
    private List<Integer> cadenaFecha = new ArrayList<>(); 
    
    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.

    @FXML
    public void initialize() {

        // Configurar columnas de la tabla utilizando PropertyValueFactory para enlazar propiedades del objeto tablaContenido.
        colIdMobiliario.setCellValueFactory(new PropertyValueFactory<>("id_Mobiliario"));
        colIdMobiliario.setCellValueFactory(new PropertyValueFactory<>("id_Mobiliario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));

        // Listener que actualiza el campo id_select cuando se selecciona un nuevo item en la tabla.
        tablaMobiliario.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id_selected = newValue.getId_Mobiliario(); // Obtiene el ID del mobiliario seleccionado.
                id_select.setText("" + id_selected); // Muestra el ID en el campo correspondiente.
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

    private void realizarConsulta() {

        listaMobiliario.clear(); // Limpiar la lista antes de realizar una nueva consulta.

        try {
            if (con != null) { // Verificar si se ha establecido una conexión.
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM mobiliario WHERE Subtipo = '" + fileName + "'");

                while (rs.next()) {
                    int idMobiliario = rs.getInt("id_Mobiliario");
                    String nombre = rs.getString("Nombre");
                    String descripcion = rs.getString("Descripcion");

                    listaMobiliario.add(new tablaContenido(idMobiliario, nombre, descripcion));
                }

                tablaMobiliario.setItems(listaMobiliario); // Asignar los datos obtenidos a la tabla.
                rs.close(); // Cerrar el ResultSet después de usarlo.
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage()); // Manejo básico de errores en caso de fallos en la consulta.
        }
    }

    public void setFileName(String fileName, int user) {
        this.fileName = fileName;
        usuario = user;
        realizarConsulta(); // Llama a realizarConsulta al establecer un nuevo nombre de archivo.
    }

    @FXML
    private Button CrearReserva;

    @FXML
    void btnCrearReserva(ActionEvent event) {

        LocalDate fecha = calendario1.getValue(); // Obtener la fecha seleccionada desde el DatePicker
        LocalDate fechaActual = LocalDate.now();
      if (fecha.isAfter(fechaActual) || fecha.isEqual(fechaActual.plusDays(1))) {


            hora_Inicio = menu_hhInicio.getText() + ":" + menu_mmInicio.getText();  // Obtener hora de inicio desde el campo correspondiente.
            hora_Fin = menu_hhTermino.getText() + ":" + menu_mmTermino.getText();   // Obtener hora de fin desde el campo correspondiente.

            if (fecha != null && hora_Inicio != null && hora_Fin != null && id_selected != 0) {  // Verificar que se ha seleccionado una fecha válida.
                dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
                fecha_Seleccionada = fecha.toString();
                boolean validarHorario = FechaSeleccionada();
                if (validarHorario) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Horario Ocupado");
                } else {
                    Insert dato = new Insert();
                     razon=menuRazon.getText();
                    dato.insertarHorario(hora_Inicio, hora_Fin, dia, fecha_Seleccionada, id_selected, razon, usuario);
                }
                JOptionPane.showInternalMessageDialog(null, "Se ha creado su reserva");  // Mostrar un diálogo interno con mensaje.

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Se ha creado su reserva");  // Crear una alerta confirmando que se ha creado la reserva.
                alerta.showAndWait();  // Mostrar la alerta y esperar a que sea cerrada por el usuario.
            } else {
                System.out.println("No se ha seleccionado ninguna fecha.");
                // Mensaje en consola si no hay fecha seleccionada.
            }

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
                ResultSet rs = st.executeQuery("SELECT * FROM detalle_reserva WHERE id_Mobiliario = '" + id_selected + "'");

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

}
