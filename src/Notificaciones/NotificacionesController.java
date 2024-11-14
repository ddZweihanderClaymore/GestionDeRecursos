package Notificaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javaapplication1.JavaApplication1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tabla.tablaContenido;

public class NotificacionesController {
    private Connection con = JavaApplication1.getConnection();
    private ObservableList<ContenidoNotificacion> detalleReserva = FXCollections.observableArrayList();

    @FXML // fx:id="colDescripcion"
    private TableColumn<tablaContenido, String> colDescripcion; // Value injected by FXMLLoader

    @FXML // fx:id="colEstado"
    private TableColumn<tablaContenido, String> colEstado; // Value injected by FXMLLoader

    @FXML // fx:id="colFecha"
    private TableColumn<tablaContenido, String> colFecha; // Value injected by FXMLLoader

    @FXML // fx:id="colId_Notificacion"
    private TableColumn<tablaContenido, Integer> colId_Notificacion; // Value injected by FXMLLoader

    @FXML // fx:id="colTitulo"
    private TableColumn<tablaContenido, String> colTitulo; // Value injected by FXMLLoader

    @FXML // fx:id="tablaDetalleReserva"
    private TableView<tablaContenido> tablaDetalleReserva; // Value injected by FXMLLoader

    public NotificacionesController() {
        // Constructor vacío
    }

    public NotificacionesController(String usuario) {
        cargarNotificaciones(usuario);
    }

    private void cargarNotificaciones(String usuario) {
        detalleReserva.clear();
        String query = "SELECT * FROM notificaciones WHERE id_trabajador = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idNotificacion = rs.getInt("id_Notificacion");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                java.sql.Timestamp fecha = rs.getTimestamp("fecha");
                String Fecha = fecha.toString(); // Mejor manejo de formato
                String estado = rs.getString("estado");

                detalleReserva.add(new ContenidoNotificacion(idNotificacion, titulo, descripcion, Fecha, estado));
            }
            tablaDetalleReserva.setItems(detalleReserva);
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());
        }
    }
}
//     public void crearNotificaciones(String usuario) {
//         try {
//            if (con != null) { // Verificar si se ha establecido una conexión.
//                Statement st = con.createStatement();
//
//                ResultSet rs_Puesto = st.executeQuery("SELECT * FROM trabajador WHERE id_trabajador = '" + usuario + "'");
//                
//                while(rs_Puesto.next()){
//                    puesto= rs_Puesto.getString("Puesto");
//                }
//                if(puesto=="Gerente"){
//                    
//                }
//                ResultSet rs = st.executeQuery("SELECT * FROM detalle_reserva WHERE id_trabajador = '" + usuario + "'");
//
//                while (rs.next()) {
//                    int idNotificacion = rs.getInt("id_detalleReserva");
//                    String titulo = rs.getString("Fecha");
//                    String descripcion = rs.getString("RazondeReserva");
//                    java.sql.Timestamp fecha = rs.getTimestamp("Estado");
//                    String estado=rs.getString("");
//                    
//                    
//                    
//               //     CREAR FXML DE NOTIFICACIONES
//                
//                
//                
//                
//                
//                }
//
//                rs.close();
//            }
//        } catch (SQLException e) {
//            System.err.println("Su bandeja esta vacia"); 
//        }
//    }
//}
