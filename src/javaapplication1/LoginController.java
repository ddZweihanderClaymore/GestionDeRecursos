package javaapplication1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {

    String puesto;
    String departamento;
    private int id_trabajador;
    
    @FXML // fx:id="Contraseña"
    private TextField Contraseña; // Value injected by FXMLLoader

    @FXML // fx:id="Usuario"
    private TextField Usuario; // Value injected by FXMLLoader

    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.
    
    @FXML
    private void validarUsuario() {
        int usuario = Integer.parseInt(Usuario.getText());
        String contra = Contraseña.getText();

        if (con != null) { 
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM trabajador WHERE id_Trabajador = ? AND Contraseña = ?")) {
                pst.setInt(1, usuario);
                pst.setString(2, contra);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) { 
                        int id_Usuario = rs.getInt("id_Trabajador"); 
                        String contraseña = rs.getString("Contraseña");

                        if (usuario == id_Usuario && contra.equals(contraseña)) {
                            id_trabajador = id_Usuario; // Asigna aquí el ID del trabajador
                            Stage currentStage = (Stage) Usuario.getScene().getWindow();
                            currentStage.close();
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML.fxml"));
                                Parent root = loader.load();
                                
                                // Obtener el controlador de FXMLController y pasar el id_trabajador
                                FXMLController fxmlController = loader.getController();
                                fxmlController.setId_trabajador(id_trabajador);
                                
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Usuario o contraseña incorrectos.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha establecido conexión con la base de datos.");
        }
    }
}
