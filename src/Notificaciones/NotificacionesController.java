package Notificaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javaapplication1.JavaApplication1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NotificacionesController {

    private int usuario;
    private Connection con = JavaApplication1.getConnection();
    private ObservableList<ContenidoNotificacion> detalleReserva = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ContenidoNotificacion, String> colDescripcion;
    @FXML
    private TableColumn<ContenidoNotificacion, String> colEstado;
    @FXML
    private TableColumn<ContenidoNotificacion, String> colFecha;
    @FXML
    private TableColumn<ContenidoNotificacion, Integer> colId_Notificacion;
    @FXML
    private TableView<ContenidoNotificacion> tablaDetalleReserva;

    public NotificacionesController() {
        // Constructor vacío
    }

    private void cargarNotificaciones() {
        detalleReserva.clear();
        System.out.println("Este es el usuario en cargarNotificaciones: " + usuario);
        String query = "SELECT * FROM detalle_reserva WHERE id_trabajador = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, usuario);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idNotificacion = rs.getInt("id_detalleReserva");
                java.sql.Timestamp fechaTimestamp = rs.getTimestamp("Fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fecha = sdf.format(fechaTimestamp);
                String descripcion = rs.getString("RazondeReserva");
                String estado = rs.getString("Estado");

                detalleReserva.add(new ContenidoNotificacion(idNotificacion, fecha, descripcion, estado));
            }
            tablaDetalleReserva.setItems(detalleReserva);
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());
        }
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
        cargarNotificaciones(); // Cargar notificaciones una vez se establece el usuario
    }

    @FXML
    public void initialize() {
        colId_Notificacion.setCellValueFactory(cellData -> cellData.getValue().Id_NotificacionProperty().asObject());
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().DescripcionProperty());
        colFecha.setCellValueFactory(cellData -> cellData.getValue().FechaProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().EstadoProperty());
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
