package javaapplication1;

import Notificaciones.NotificacionesController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import tabla.tablaContenido;
import tabla.tablaController;

public class FXMLController implements Initializable {

    // Paneles de la interfaz de usuario
    @FXML
    private AnchorPane opciones;  // Panel de opciones
    @FXML
    private AnchorPane calendario; // Panel de calendario
    @FXML
    private AnchorPane tabla;      // Panel para mostrar tablas

    // Campos de entrada de texto
    @FXML
    private TextField Usuario;      // Campo para ingresar el ID del usuario
    @FXML
    private TextField Puesto;       // Campo para mostrar el puesto del usuario

    // Variables privadas para la lógica del controlador
    private int user;                // ID del usuario actual
    private int i = 0;               // Contador auxiliar
    private List<Integer> cadenaUsuarios = new ArrayList<>(); // Lista de IDs de usuarios en el mismo departamento
    public String btn_Solicitud;     // Variable para almacenar la solicitud del botón (no utilizada en este fragmento)
    
    private int id_trabajador;       // ID del trabajador actual
    private int departamento;         // ID del departamento del trabajador actual
    
    // Conexión a la base de datos
    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.
    
    private String puesto;            // Puesto del trabajador actual

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria (actualmente vacía)
    }

    /**
     * Método que maneja la selección de un elemento del menú.
     *
     * @param event Evento de acción generado por la selección del menú.
     */
    @FXML
    private void Seleccionado(ActionEvent event) {
        user = Integer.parseInt(Usuario.getText());  // Obtener el ID del usuario desde el campo de texto
        
        MenuItem selectedMenuItem = (MenuItem) event.getSource();  // Obtener el menú seleccionado
        String texto = selectedMenuItem.getText();  // Texto del menú seleccionado
        
        System.out.println("Texto del MenuItem seleccionado: " + texto);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tabla/table.fxml"));  // Cargar el FXML correspondiente a la tabla
            Parent root = loader.load();

            // Obtener el controlador de `tablaController`
            tablaController controller = loader.getController();
            controller.setFileName(texto, user);  // Enviar el nombre del botón y el ID del usuario

            // Limpiar el contenido de `tabla` y añadir el contenido de `table.fxml`
            tabla.getChildren().clear();
            tabla.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();  // Manejo básico de errores en caso de fallos al cargar el FXML.
        }
    }

    /**
     * Método que maneja la acción al presionar el botón de notificación.
     *
     * @param event Evento de acción generado por la presión del botón.
     */
    @FXML
    private void btn_Notificacion(ActionEvent event) {
        MenuItem selectedMenuItem = (MenuItem) event.getSource();  // Obtener el menú seleccionado
        String texto = selectedMenuItem.getText();  // Texto del menú seleccionado
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Notificaciones/Notificacion.fxml"));  // Cargar el FXML de notificaciones
            Parent root = loader.load();

            // Obtener el controlador de `NotificacionesController`
            NotificacionesController controller = loader.getController();
            
            try {
                if (con != null) {  // Verificar si se ha establecido una conexión.
                    Statement st = con.createStatement();
                    user = Integer.parseInt(Usuario.getText());  // Obtener el ID del usuario
                    
                    ResultSet rs = st.executeQuery("SELECT * FROM trabajador WHERE id_Trabajador = '" + user + "'");
                    
                    while (rs.next()) {
                        puesto = rs.getString("Puesto"); 
                        departamento = rs.getInt("id_Departamento");
                    }
                    
                    if (puesto.equals("Gerente") || puesto.equals("Gestor de activos")) {
                        System.out.println("Entro al if");
                        
                        ResultSet rsCadena = st.executeQuery("SELECT * FROM trabajador WHERE id_Departamento = '" + departamento + "'"); 
                        
                        while (rsCadena.next()) {
                            int idTrabajador = rsCadena.getInt("id_Trabajador");  // Usar rsCadena aquí
                            cadenaUsuarios.add(idTrabajador);  // Añadir el ID a la lista
                            System.out.println("ID Trabajador: " + idTrabajador);
                        }
                    }
                    
                    controller.setUsuario(cadenaUsuarios, texto);  // Enviar lista de usuarios al controlador
                    
                    rs.close();  // Cerrar el ResultSet después de usarlo.
                }
            } catch (SQLException e) {
                System.err.println("Error al realizar la consulta: " + e.getMessage());  // Manejo básico de errores en caso de fallos en la consulta.
            }

            // Limpiar el contenido de `tabla` y añadir el contenido de `Notificacion.fxml`
            tabla.getChildren().clear();
            tabla.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();  // Manejo básico de errores en caso de fallos al cargar el FXML.
        }
    }

    /**
     * Método para obtener el ID del trabajador.
     *
     * @return El ID del trabajador.
     */
    public int getId_trabajador() {
        return id_trabajador;  
    }

    /**
     * Método para establecer el ID del trabajador.
     *
     * @param id_trabajador El ID a establecer.
     */
    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;  
        
        if (Usuario != null) {
            Usuario.setText("" + this.id_trabajador);  // Actualizar campo Usuario con el ID establecido.
        }
    }

    /**
     * Método para obtener el puesto del trabajador.
     *
     * @return El puesto del trabajador.
     */
    public int getPuesto() {
        return id_trabajador;  
    }

    /**
     * Método para establecer el puesto del trabajador.
     *
     * @param puesto El puesto a establecer.
     */
    public void setPuesto(String puesto) {
        this.puesto = puesto;  
        
        if (Puesto != null) {
            Puesto.setText(this.puesto);  // Actualizar campo Puesto con el puesto establecido.
        }
    }
}