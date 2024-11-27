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

    // Variables de instancia para almacenar información del usuario
    private String puesto;            // Puesto del trabajador
    String departamento;              // Departamento del trabajador (no utilizado en este fragmento)
    private int id_trabajador;       // ID del trabajador

    // Elementos de la interfaz de usuario
    @FXML // fx:id="Contraseña"
    private TextField Contraseña;    // Campo para ingresar la contraseña (Value injected by FXMLLoader)

    @FXML // fx:id="Usuario"
    private TextField Usuario;        // Campo para ingresar el ID del usuario (Value injected by FXMLLoader)
    
    @FXML
    private Label Alerta;             // Etiqueta para mostrar mensajes de alerta

    // Conexión a la base de datos
    Connection con = JavaApplication1.getConnection(); // Obtener conexión desde JavaApplication1.

    /**
     * Método que valida las credenciales del usuario.
     */
    @FXML
    private void validarUsuario() {
        int usuario;                    // Variable para almacenar el ID del usuario ingresado
        String contra = Contraseña.getText();  // Obtener la contraseña ingresada

        // Validar que el usuario sea un número válido
        try {
            usuario = Integer.parseInt(Usuario.getText());  // Convertir el texto a número
        } catch (NumberFormatException e) {
            Alerta.setText("El usuario debe ser un número válido.");  // Mensaje de error si no es un número
            Alerta.setVisible(true);
            return;  // Salir del método si el usuario no es válido
        }

        if (con != null) {  // Verificar si la conexión a la base de datos es válida
            try {
                // Verificar si el usuario existe en la base de datos
                try (PreparedStatement checkUserStmt = con.prepareStatement(
                        "SELECT * FROM trabajador WHERE id_Trabajador = ?")) {
                    checkUserStmt.setInt(1, usuario);  // Establecer el ID del trabajador en la consulta

                    try (ResultSet userRs = checkUserStmt.executeQuery()) {
                        if (userRs.next()) {  // Si se encuentra al usuario
                            // El usuario existe, verificar la contraseña
                            String correctPassword = userRs.getString("Contraseña");  // Obtener la contraseña correcta
                            puesto = userRs.getString("Puesto");  // Obtener el puesto del trabajador

                            if (contra.equals(correctPassword)) {  // Comparar contraseñas
                                // Credenciales válidas, proceder a abrir la siguiente ventana
                                id_trabajador = usuario;  // Guardar el ID del trabajador

                                Stage currentStage = (Stage) Usuario.getScene().getWindow();  // Obtener la ventana actual
                                if (currentStage != null) {
                                    currentStage.close();  // Cerrar la ventana actual
                                }

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML.fxml"));  // Cargar el siguiente FXML
                                Parent root = loader.load();

                                FXMLController fxmlController = loader.getController();  // Obtener el controlador de la nueva ventana
                                fxmlController.setId_trabajador(id_trabajador);  // Pasar el ID del trabajador al controlador
                                fxmlController.setPuesto(puesto);  // Pasar el puesto al controlador

                                Stage stage = new Stage();  // Crear una nueva ventana
                                 stage.setTitle("Sistema de reservas");
                                stage.setScene(new Scene(root));  // Establecer la escena en la nueva ventana
                                stage.show();  // Mostrar la nueva ventana
                            } else {
                                // Contraseña incorrecta, mostrar mensaje de error
                                Alerta.setText("La contraseña ingresada es incorrecta.");
                                Alerta.setVisible(true);
                            }
                        } else {
                            // Usuario no encontrado, mostrar mensaje de error
                            Alerta.setText("El usuario ingresado no existe.");
                            Alerta.setVisible(true);
                        }
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();  // Manejo básico de errores en caso de fallos en la consulta o carga del FXML.
                Alerta.setText("Ocurrió un problema. Por favor, intente nuevamente.");  // Mensaje genérico de error
                Alerta.setVisible(true);
            }
        } else {
            Alerta.setText("No se pudo establecer conexión con la base de datos.");  // Mensaje si no hay conexión a la base de datos
            Alerta.setVisible(true);
        }
    }
}