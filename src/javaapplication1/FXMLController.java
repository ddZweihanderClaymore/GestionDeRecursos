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

    @FXML
    private AnchorPane opciones; // Panel de opciones
    @FXML
    private AnchorPane calendario; // Panel de calendario
    @FXML
    private AnchorPane tabla;
    @FXML
    private TextField Usuario;
    @FXML
    private TextField Puesto;    

    private int user;
    private int i=0;
    private List<Integer> cadenaUsuarios = new ArrayList<>(); 
    public String btn_Solicitud;
    private int id_trabajador;
    private int departamento;
    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.
    private String puesto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    @FXML
    private void Seleccionado(ActionEvent event) {
        int user = Integer.parseInt(Usuario.getText());
        
        MenuItem selectedMenuItem = (MenuItem) event.getSource();
        String texto = selectedMenuItem.getText();
        System.out.println("Texto del MenuItem seleccionado: " + texto);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tabla/table.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de `tablaController`
            tablaController controller = loader.getController();
            controller.setFileName(texto, user);  // Enviar el nombre del botón

            // Limpiar el contenido de `tabla` y añadir el contenido de `table.fxml`
            tabla.getChildren().clear();
            tabla.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_Notificacion(ActionEvent event) {
        MenuItem selectedMenuItem = (MenuItem) event.getSource();
        String texto = selectedMenuItem.getText();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Notificaciones/Notificacion.fxml"));
            Parent root = loader.load();
            // Obtener el controlador de `NotificacionesController`
            NotificacionesController controller = loader.getController();
            try {
                if (con != null) { // Verificar si se ha establecido una conexión.
                    
                    Statement st = con.createStatement();
                     user = Integer.parseInt(Usuario.getText());
                    cadenaUsuarios.add(user);
                    ResultSet rs = st.executeQuery("SELECT * FROM trabajador WHERE id_Trabajador = '" + user + "'");
                    while (rs.next()) {
                       puesto=rs.getString("Puesto"); 
                       departamento= rs.getInt("id_Departamento");
                    }
                    
                    if(puesto.equals("Gerente")||puesto.equals("Gestor de activos")){
                        System.out.println("Entro al if");
                        
                        ResultSet rsCadena = st.executeQuery("SELECT * FROM trabajador WHERE id_Departamento = '" + departamento + "'"); 
                     
                        while (rsCadena.next()) {
                            int idTrabajador = rsCadena.getInt("id_Trabajador"); // Usar rsCadena aquí
                            cadenaUsuarios.add(idTrabajador); // Añadir el ID a la lista
                            System.out.println("ID Trabajador: " + idTrabajador);
                        } // Cambié setFileName a setUsuario
                    }
                    
                          controller.setUsuario(cadenaUsuarios, texto);  

                    

                    rs.close(); // Cerrar el ResultSet después de usarlo.
                }
            } catch (SQLException e) {
                System.err.println("Error al realizar la consulta: " + e.getMessage()); // Manejo básico de errores en caso de fallos en la consulta.
            }

            // Limpiar el contenido de `tabla` y añadir el contenido de `Notificacion.fxml`
            tabla.getChildren().clear();
            tabla.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Métodos para obtener y establecer el id_trabajador
    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
         if (Usuario != null) {
            Usuario.setText(""+this.id_trabajador); 
        }
    }
    
        public int getPuesto() {
        return id_trabajador;
    }

    public void setPuesto(String puesto) {
         if (Puesto != null) {
            Puesto.setText(puesto); 
        }
    }
}
