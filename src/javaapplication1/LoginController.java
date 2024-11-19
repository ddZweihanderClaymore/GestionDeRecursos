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
import javafx.scene.control.Label;

public class LoginController {

    String puesto;
    String departamento;
    private int id_trabajador;

    @FXML // fx:id="Contraseña"
    private TextField Contraseña; // Value injected by FXMLLoader

    @FXML // fx:id="Usuario"
    private TextField Usuario; // Value injected by FXMLLoader
    @FXML
    private Label Alerta;
    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.

    @FXML
    private void validarUsuario() {
        int usuario;
        String contra = Contraseña.getText();

        // Validar que el usuario sea un número válido
        try {
            usuario = Integer.parseInt(Usuario.getText());
        } catch (NumberFormatException e) {
            Alerta.setText("El usuario debe ser un número válido.");
            Alerta.setVisible(true);
            return;
        }

        if (con != null) {
            try {
                // Verificar si el usuario existe
                try (PreparedStatement checkUserStmt = con.prepareStatement(
                        "SELECT Contraseña FROM trabajador WHERE id_Trabajador = ?")) {
                    checkUserStmt.setInt(1, usuario);

                    try (ResultSet userRs = checkUserStmt.executeQuery()) {
                        if (userRs.next()) {
                            // El usuario existe, verificar la contraseña
                            String correctPassword = userRs.getString("Contraseña");
                            if (contra.equals(correctPassword)) {
                                // Credenciales válidas
                                id_trabajador = usuario;
                                Stage currentStage = (Stage) Usuario.getScene().getWindow();
                                if (currentStage != null) {
                                    currentStage.close();
                                }
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML.fxml"));
                                Parent root = loader.load();

                                FXMLController fxmlController = loader.getController();
                                fxmlController.setId_trabajador(id_trabajador);

                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } else {
                                // Contraseña incorrecta
                                Alerta.setText("La contraseña ingresada es incorrecta.");
                                Alerta.setVisible(true);
                            }
                        } else {
                            // Usuario no encontrado
                            Alerta.setText("El usuario ingresado no existe.");
                            Alerta.setVisible(true);
                        }
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alerta.setText("Ocurrió un problema. Por favor, intente nuevamente.");
                Alerta.setVisible(true);
            }
        } else {
            Alerta.setText("No se pudo establecer conexión con la base de datos.");
            Alerta.setVisible(true);
        }
    }

}
