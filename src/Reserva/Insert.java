/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javaapplication1.JavaApplication1;
import javafx.scene.control.Alert;

/**
 * Clase que maneja la inserción de datos en las tablas relacionadas con
 * reservas. Incluye métodos para insertar horarios, detalles de reservas y
 * reservas.
 *
 * @author parca
 */
public class Insert {

    // Variables de instancia
    private String privilegio;          // Privilegios del usuario (no utilizado en este fragmento)
    private int trabajador;             // ID del trabajador actual
    private int id_trabajador;          // ID del trabajador (no utilizado en este fragmento)
    private String razon;               // Razón para la reserva
    Alert alerta;

    // Conexión a la base de datos
    Connection con = JavaApplication1.getConnection();
    

    /**
     * Constructor de la clase Insert.
     */
    public Insert() {
    }

    /**
     * Método que inserta un horario en la base de datos.
     *
     * @param hora_Inicio Hora de inicio de la reserva.
     * @param hora_Fin Hora de fin de la reserva.
     * @param dia Día de la semana de la reserva.
     * @param fecha Fecha de la reserva.
     * @param id_selected ID del mobiliario seleccionado.
     * @param razon Razón para la reserva.
     * @param usuario ID del usuario que realiza la reserva.
     */
    public void insertarHorario(String hora_Inicio, String hora_Fin, String dia, String fecha, int id_selected, String razon, int usuario) {
       java.sql.Date fechaSql;
        try {
            if (con != null) {  // Verifica que la conexión no es nula

                // INSERTAR HORARIO
                String inserthorarioSQL = "INSERT INTO horario (Hora_inicio, Hora_Termino, Dia_Semana, FechaReserva) VALUES (?,?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(inserthorarioSQL);
                ps.setString(1, hora_Inicio);  // Establecer hora de inicio
                ps.setString(2, hora_Fin);      // Establecer hora de fin
                ps.setString(3, dia);           // Establecer día de la semana
                ps.setString(4, fecha);
                    System.out.println("Reservar horario\n"
                            + "Consulta"+ps.toString());
                int rowsInserted = ps.executeUpdate();  // Ejecutar la consulta
                if (rowsInserted > 0) {
                    System.out.println("Inserción exitosa en la tabla horario.");
                } else {
                    System.out.println("Inserción fallida en la tabla horario.");
                }

                // INSERTAR DETALLE RESERVA
                Statement stHorario = con.createStatement();
                ResultSet rsHorario = stHorario.executeQuery("SELECT MAX(id_Horario) FROM horario");

                int newId = 1;  // Inicializar nuevo ID
                if (rsHorario.next()) {
                    newId = rsHorario.getInt(1) + 1;  // Obtener el nuevo ID basado en el máximo existente
                }
                LocalDate fechaActual = LocalDate.now();
                fechaSql = java.sql.Date.valueOf(fechaActual); 
                String insertdetalleReservaSQL = "INSERT INTO detalle_reserva (id_Horario, id_Mobiliario, id_Trabajador, Fecha, RazondeReserva, Estado) VALUES (?, ?, ?, ?, ?,?)";
                PreparedStatement psdetallereserva = con.prepareStatement(insertdetalleReservaSQL);
                psdetallereserva.setInt(1, newId - 1);  // ID del horario insertado
                psdetallereserva.setInt(2, id_selected); // ID del mobiliario seleccionado
                psdetallereserva.setInt(3, usuario);     // ID del usuario que realiza la reserva
                psdetallereserva.setDate(4, fechaSql);   // Fecha de la reserva
                psdetallereserva.setString(5, razon);     // Razón para la reserva
                psdetallereserva.setString(6, "Por validar");  // Estado inicial
                int rowsInserteddetalleReserva = psdetallereserva.executeUpdate();  // Ejecutar la consulta
                if (rowsInserteddetalleReserva > 0) {
                    System.out.println("Inserción exitosa en la tabla detalle_reserva.");
                    alerta = new Alert(Alert.AlertType.CONFIRMATION, "Formulario en espera de aprobacion.");
                    alerta.showAndWait();
                } else {
                    System.out.println("Inserción fallida en  el formulario.");
                    alerta = new Alert(Alert.AlertType.CONFIRMATION, "Inserción fallida en el formulario.");
                    alerta.showAndWait();
                }

                ps.close();  // Cerrar el PreparedStatement después de usarlo
                psdetallereserva.close();
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());  // Manejo básico de errores en caso de fallos en las consultas.
        }
    }

    /**
     * Método que crea una nueva reserva en la base de datos.
     */
    public void crearReserva(int detalleReserva, String comentarios, int aprobado) {
        try {
            if (con != null) {  // Verifica que la conexión no es nula

               
                String insertreservaSQL = "INSERT INTO reserva (id_gerente, id_DetalleReserva, Comentario) VALUES (?, ?, ?)";
                PreparedStatement psreserva = con.prepareStatement(insertreservaSQL);
                psreserva.setInt(1, aprobado);                  // Establecer ID del trabajador
                psreserva.setInt(2, detalleReserva);   // Establecer ID del detalle reserva creado anteriormente
                psreserva.setString(3, comentarios);                    // Establecer razón para la reserva

                int rowsInsertedReserva = psreserva.executeUpdate();  // Ejecutar la consulta
                if (rowsInsertedReserva > 0) {
                    String sql = "UPDATE detalle_reserva SET Estado=?";
                    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                        // Establecer los parámetros
                        pstmt.setString(1, "Validado");

                        // Ejecutar la actualización
                        int filasActualizadas = pstmt.executeUpdate();
                         if( filasActualizadas>0){
                              alerta = new Alert(Alert.AlertType.CONFIRMATION, "Se actualizo el estado.");
                              alerta.showAndWait();
                         }
                    } catch (SQLException e) {
                        e.printStackTrace(); // Manejo de excepciones
                    }
                       
                    alerta = new Alert(Alert.AlertType.CONFIRMATION, "Inserción exitosa en la tabla reserva.");
                    alerta.showAndWait();
                } else {
                    alerta = new Alert(Alert.AlertType.CONFIRMATION, "Inserción fallida en la tabla reserva.");
                    alerta.showAndWait();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());  // Manejo básico de errores en caso de fallos en las consultas.
        }
    }

    public void editarHorario(int idHorario, int id_detalleReserva, String hora_Inicio, String hora_Fin, String dia, LocalDate fecha, String razon) {
        java.sql.Date fechaSql;
        if (con != null) {
            // Verifica que la conexión no es nula
            fechaSql = java.sql.Date.valueOf(fecha); 
            String sql = "UPDATE horario SET Hora_inicio = ?, Hora_Termino = ?, Dia_Semana = ? FechaReserva= ? WHERE id_Horario = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                // Establecer los parámetros
                pstmt.setString(1, hora_Inicio);
                pstmt.setString(2, hora_Fin);
                pstmt.setString(3, dia);
                pstmt.setDate(4, fechaSql);   // Fecha de la reserva
                pstmt.setInt(5, idHorario);

                // Ejecutar la actualización
                int filasActualizadas = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }

            sql = "UPDATE detalle_reserva SET RazondeReserva = ? WHERE id_detalleReserva = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                // Establecer los parámetros
                pstmt.setString(1, razon);
                pstmt.setInt(2, id_detalleReserva);

                // Ejecutar la actualización
                int filasActualizadas = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }
        } // Manejo básico de errores en caso de fallos en las consultas.
    }
public void Rechazar(String comentario, int id_detalleReserva) {
        java.sql.Date fechaSql;
        if (con != null) {
            
            String sql = "UPDATE detallereserva SET Estado = ? WHERE id_detalleReserva = ?";
            
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                // Establecer los parámetros
                pstmt.setString(1, comentario);
                pstmt.setInt(2, id_detalleReserva);

                // Ejecutar la actualización
                int filasActualizadas = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }
        } // Manejo básico de errores en caso de fallos en las consultas.
    }

    /**
     * Método que crea una nueva reserva en la base de datos.
     */
    public void editarReserva() {
        try {
            if (con != null) {  // Verifica que la conexión no es nula

                // INSERTAR RESERVA
                Statement stDetalleReserva = con.createStatement();
                ResultSet rsDetalleReserva = stDetalleReserva.executeQuery("SELECT MAX(id_DetalleReserva) FROM detalle_reserva");

                int newIdDetalleReserva = 1;  // Inicializar nuevo ID para detalle reserva
                if (rsDetalleReserva.next()) {
                    newIdDetalleReserva = rsDetalleReserva.getInt(1) + 1;  // Obtener el nuevo ID basado en el máximo existente
                }

                String insertreservaSQL = "INSERT INTO reserva (id_Trabajador, id_DetalleReserva, razon) VALUES (?, ?, ?)";
                PreparedStatement psreserva = con.prepareStatement(insertreservaSQL);
                psreserva.setInt(1, trabajador);                  // Establecer ID del trabajador
                psreserva.setInt(2, newIdDetalleReserva - 1);   // Establecer ID del detalle reserva creado anteriormente
                psreserva.setString(3, razon);                    // Establecer razón para la reserva

                int rowsInsertedReserva = psreserva.executeUpdate();  // Ejecutar la consulta
                if (rowsInsertedReserva > 0) {
                    System.out.println("Inserción exitosa en la tabla reserva.");
                } else {
                    System.out.println("Inserción fallida en la tabla reserva.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());  // Manejo básico de errores en caso de fallos en las consultas.
        }
    }

    /**
     * Método para obtener el privilegio del usuario.
     *
     * @return El privilegio del usuario.
     */
    public String getPrivilegio() {
        return privilegio;  // Retorna el privilegio del usuario
    }

    /**
     * Método para establecer el privilegio del usuario.
     *
     * @param privilegio El privilegio a establecer.
     */
    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;  // Establece el privilegio del usuario
    }
}
