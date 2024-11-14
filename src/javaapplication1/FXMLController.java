package javaapplication1;

import Notificaciones.NotificacionesController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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

    
    public String btn_Solicitud;
    private int id_trabajador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    @FXML
    private void Seleccionado(ActionEvent event) {
        MenuItem selectedMenuItem = (MenuItem) event.getSource();
        String texto = selectedMenuItem.getText();
        System.out.println("Texto del MenuItem seleccionado: " + texto);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tabla/table.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de `tablaController`
            tablaController controller = loader.getController();
            controller.setFileName(texto);  // Enviar el nombre del botón

            // Limpiar el contenido de `tabla` y añadir el contenido de `table.fxml`
            tabla.getChildren().clear();
            tabla.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_Notificacion(ActionEvent event) {
       NotificacionesController notificacion= new NotificacionesController();
       
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
}
