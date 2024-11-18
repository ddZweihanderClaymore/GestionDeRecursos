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
import javaapplication1.JavaApplication1;
import tabla.tablaContenido;

/**
 * AQUI SE HARAN LOS INSERT
 *
 * @author parca
 */
public class Insert {

    public Insert() {
    }

    private String privilegio;
    private int trabajador;
    private int id_trabajador;
    private String razon;

    Connection con = JavaApplication1.getConnection();

    public void insertarHorario(String hora_Inicio, String hora_Fin, String dia, String fecha, int id_selected, String razon, int usuario) {
        try {
            if (con != null) {  // Verifica que la conexión no es nula
                
                //INSERT HORARIO
                String inserthorarioSQL = "INSERT INTO horario (Hora_inicio, Hora_Termino, Dia_Semana) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(inserthorarioSQL);
                ps.setString(1, hora_Inicio);  // Hora de inicio obtenida del campo de texto
                ps.setString(2, hora_Fin);  // Hora de fin obtenida del campo de texto
                ps.setString(3, dia);  // Día de la semana seleccionado

                int rowsInserted = ps.executeUpdate();  // Ejecuta la consulta
                if (rowsInserted > 0) {
                    System.out.println("Inserción exitosa en la tabla horario.");
                } else {
                    System.out.println("Inserción fallida en la tabla horario.");
                }

                //INSERT DETALLE RESERVA
                Statement stHorario = con.createStatement();
                ResultSet rsHorario = stHorario.executeQuery("SELECT MAX(id_Horario) FROM horario");

                int newId = 1;
                if (rsHorario.next()) {
                    newId = rsHorario.getInt(1) + 1;
                }
                String insertdetalleReservaSQL = "INSERT INTO detalle_reserva ( id_Horario, id_Mobiliario, id_Trabajador, Fecha, Estado ) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psdetallereserva = con.prepareStatement(insertdetalleReservaSQL);
                psdetallereserva.setInt(1, newId - 1);  // Hora de inicio obtenida del campo de texto
                psdetallereserva.setInt(2, id_selected); // Hora de fin obtenida del campo de texto
                psdetallereserva.setInt(3, usuario);
                psdetallereserva.setString(4, fecha);  // Día de la semana seleccionado
                psdetallereserva.setString(5,razon);
                psdetallereserva.setString(6,"Por validar");
                int rowsInserteddetalleReserva = psdetallereserva.executeUpdate();  // Ejecuta la consulta
                if (rowsInserteddetalleReserva > 0) {
                    System.out.println("Inserción exitosa en la tabla detalle_reserva.");
                } else {
                    System.out.println("Inserción fallida en la tabla detalle_reserva.");
                }

      
                ps.close();  // Cierra el PreparedStatement después de usarlo
                psdetallereserva.close();
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
    }
    }
    
    public void crearReserva() {
//        try {
//            if (con != null) {
               //INSERT RESERVA
//                Statement stDetalleReserva = con.createStatement();
//                ResultSet rsDetalleReserva = stDetalleReserva.executeQuery("SELECT MAX(id_DetalleReserva) FROM detalle_reserva");
//
//                int newIdDetalleReserva = 1;
//                if (rsDetalleReserva.next()) {
//                    newIdDetalleReserva = rsDetalleReserva.getInt(1) + 1;
//                }
//                System.out.println("trabajador: " + trabajador + "  id_detalleReserva: " + newIdDetalleReserva);
//                String insertreservaSQL = "INSERT INTO reserva (id_Trabajador, id_DetalleReserva, razon) VALUES (?, ?, ?)";
//                PreparedStatement psreserva = con.prepareStatement(insertreservaSQL);
//                psreserva.setInt(1, trabajador);  // Hora de inicio obtenida del campo de texto
//                psreserva.setInt(2, newIdDetalleReserva - 1);  // Hora de fin obtenida del campo de texto
//                psreserva.setString(3, razon);  // Día de la semana seleccionado
//
//                int rowsInsertedReserva = psreserva.executeUpdate();  // Ejecuta la consulta
//                if (rowsInsertedReserva > 0) {
//                    System.out.println("Inserción exitosa en la tabla reserva.");
//                } else {
//                    System.out.println("Inserción fallida en la tabla reserva.");
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
    }
    
    public String getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }



}
