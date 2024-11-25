/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication1.JavaApplication1;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FormularioController {
    private int id_detalleReserva;  // Obtener el ID detalleReserva.
    private int id_Horario;  // Obtener el ID Horario.
    private int id_Mobiliario;  // Obtener el id_Mobiliario.
    private int id_Trabajador;  // Obtener el id_Trabajador.
    private String fecha;  // Formatear la fecha a String.
    private String descripcion;  // Obtener la descripción de la reserva.
    private String estadoReserva;  // Obtener el estado de la reserva.   
    private int folioReserva;
    private int datosPersonales;
    private int departamento;
    private String puesto;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String celular;
    private String correo;
    private String comentario;
    private String mobiliarioNombre;
    private String mobiliarioDescripcion;
    private String mobiliarioTipo;
    private String horaInicio;
    private String horaTermino;
    private String dia;
    private String nombreDepartamento;
    private int id_Departamento;
    private int id_Reserva;
    private boolean hasResults;
    
    private Connection con = JavaApplication1.getConnection(); // Conexión a la base de datos

    @FXML
    private TextField tf_Aprobado;

    @FXML
    private TextField tf_AprobadoApellidoM;

    @FXML
    private TextField tf_AprobadoApellidoP;

    @FXML
    private TextField tf_AprobadoDepartamento;

    @FXML
    private TextField tf_AprobadoNombre;

    @FXML
    private TextField tf_AprobadoPuesto;

    @FXML
    private TextField tf_Empleado;

    @FXML
    private TextField tf_EmpleadoApellidoM;

    @FXML
    private TextField tf_EmpleadoApellidoP;

    @FXML
    private TextField tf_EmpleadoCorreo;

    @FXML
    private TextField tf_EmpleadoDepartamento;

    @FXML
    private TextField tf_EmpleadoNombre;

    @FXML
    private TextField tf_EmpleadoPuesto;

    @FXML
    private TextField tf_EmpleadoTelefono;

    @FXML
    private TextField tf_Folio;

    @FXML
    private TextField tf_FolioDescripcion;

    @FXML
    private TextField tf_FolioDia;

    @FXML
    private TextField tf_FolioEstado;

    @FXML
    private TextField tf_FolioFecha;

    @FXML
    private TextField tf_FolioInicio;

    @FXML
    private TextField tf_FolioMobiliario;

    @FXML
    private TextField tf_FolioMotivo;

    @FXML
    private TextField tf_FolioNombre;

    @FXML
    private TextField tf_FolioTermino;

    @FXML
    private TextField tf_FolioTipo;
    @FXML
    private TextField tf_Comentario;
    
    @FXML
    public void initialize() {

    }

    public void folioReserva(int folio) {
        folioReserva = folio;
        String query = "SELECT * FROM detalle_reserva WHERE id_detalleReserva = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, folioReserva);  // Establecer el ID del folio en la consulta.
            try (ResultSet rs = pst.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  // Formato de fecha.
                while (rs.next()) {  // Iterar sobre los resultados de la consulta.
                    id_detalleReserva = rs.getInt("id_detalleReserva");  // Obtener el ID detalleReserva.
                    id_Horario = rs.getInt("id_Horario");  // Obtener el ID Horario.
                    id_Mobiliario = rs.getInt("id_Mobiliario");  // Obtener el id_Mobiliario.
                    id_Trabajador = rs.getInt("id_Trabajador");  // Obtener el id_Trabajador.
                    java.sql.Timestamp fechaTimestamp = rs.getTimestamp("Fecha");  // Obtener la fecha como Timestamp.
                    fecha = sdf.format(fechaTimestamp);  // Formatear la fecha a String.
                    descripcion = rs.getString("RazondeReserva");  // Obtener la descripción de la reserva.
                    estadoReserva = rs.getString("Estado");  // Obtener el estado de la reserva.   
                }
                tf_Empleado.setText(""+id_Trabajador);
                tf_Folio.setText(""+folioReserva);
                tf_FolioFecha.setText(""+fecha);
                tf_FolioMotivo.setText(""+descripcion);
                tf_FolioEstado.setText(""+estadoReserva);
                tf_FolioMobiliario.setText(""+id_Mobiliario);
            }
            trabajador(id_Trabajador);
            mobiliario(id_Mobiliario);
            Horario(id_Horario);
            String queryreserva = "SELECT * FROM reserva WHERE id_DetalleReserva = ?";
             hasResults = false;
            try (PreparedStatement pst_reserva = con.prepareStatement(queryreserva)) {
                pst_reserva.setInt(1, folioReserva);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_reserva = pst_reserva.executeQuery()) {
                     
                      while (rs_reserva.next()) {  // Iterar sobre los resultados de la consulta.
                          hasResults=true;
                          id_Reserva= rs_reserva.getInt("id_reserva");
                          id_Trabajador= rs_reserva.getInt("id_gerente");
                          comentario= rs_reserva.getString("Comentario");
                          
                      }
                      if(hasResults){
                      tf_Folio.setText(""+id_Reserva);
                      trabajador( id_Trabajador);
                      tf_Aprobado.setText(""+id_Trabajador);
                      tf_Comentario.setText(""+comentario);
                     }
                  }

            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las notificaciones: " + e.getMessage());
        }
    }
    
    
    public void trabajador(int id_Trabajador){
        String querytrabajador = "SELECT * FROM trabajador WHERE id_Trabajador = ?";
            try (PreparedStatement pst_trabajador = con.prepareStatement(querytrabajador)) {
                pst_trabajador.setInt(1, id_Trabajador);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_Trabajador = pst_trabajador.executeQuery()) {
                      while (rs_Trabajador.next()) {  // Iterar sobre los resultados de la consulta.
                          id_Departamento=rs_Trabajador.getInt("id_Departamento");
                          datosPersonales= rs_Trabajador.getInt("id_DatosPersonales");
                          departamento= rs_Trabajador.getInt("id_Departamento");
                          puesto= rs_Trabajador.getString("Puesto");
                          
                      }
                  }
                  if(hasResults){
                      tf_AprobadoPuesto.setText(""+puesto);
                  }else{
                      tf_EmpleadoPuesto.setText(""+puesto);
                  }

                   datosPersonales(datosPersonales);
                   departamento(id_Departamento);
            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
    }
    
    
       public void datosPersonales(int datosPersonales){
           String querydatosPersonales = "SELECT * FROM datospersonales WHERE id_DatosPersonales = ?";
            try (PreparedStatement pst_datosPersonales = con.prepareStatement(querydatosPersonales)) {
                pst_datosPersonales.setInt(1, datosPersonales);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_datosPersonales = pst_datosPersonales.executeQuery()) {
                      while (rs_datosPersonales.next()) {  // Iterar sobre los resultados de la consulta.
                          nombre= rs_datosPersonales.getString("Nombre");
                          apellidoP= rs_datosPersonales.getString("ApellidoP");
                          apellidoM= rs_datosPersonales.getString("ApellidoM");
                          celular= rs_datosPersonales.getString("Celular1");
                          correo= rs_datosPersonales.getString("Correo");
                          
                      }
                      
                      if(hasResults){ 
                          tf_AprobadoNombre.setText(""+nombre);
                          tf_AprobadoApellidoP.setText(""+apellidoP);
                          tf_AprobadoApellidoM.setText(""+apellidoM);
                      }else{
                          tf_EmpleadoNombre.setText(""+nombre);
                          tf_EmpleadoApellidoP.setText(""+apellidoP);
                          tf_EmpleadoApellidoM.setText(""+apellidoM);
                          tf_EmpleadoTelefono.setText(""+celular);
                          tf_EmpleadoCorreo.setText(""+correo);
                      }
                  }

            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
       }
       
       
        public void mobiliario(int id_Mobiliario){
        String querymobiliario = "SELECT * FROM mobiliario WHERE id_Mobiliario = ?";
            try (PreparedStatement pst_mobiliario = con.prepareStatement(querymobiliario)) {
                pst_mobiliario.setInt(1, id_Mobiliario);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_mobiliario = pst_mobiliario.executeQuery()) {
                      while (rs_mobiliario.next()) {  // Iterar sobre los resultados de la consulta.
                          mobiliarioNombre= rs_mobiliario.getString("Nombre");
                          mobiliarioDescripcion= rs_mobiliario.getString("Descripcion");
                          mobiliarioTipo= rs_mobiliario.getString("Tipo");
                      }
                  }
                  tf_FolioNombre.setText(""+mobiliarioNombre);
                  tf_FolioDescripcion.setText(""+mobiliarioDescripcion);
                  tf_FolioTipo.setText(""+mobiliarioTipo);
                  

            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
    }

        
        public void Horario(int id_Horario){
        String queryHorario = "SELECT * FROM horario WHERE id_Horario = ?";
            try (PreparedStatement pst_Horario = con.prepareStatement(queryHorario)) {
                pst_Horario.setInt(1, id_Horario);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_Horario = pst_Horario.executeQuery()) {
                      while (rs_Horario.next()) {  // Iterar sobre los resultados de la consulta.
                          horaInicio= rs_Horario.getString("Hora_inicio");
                          horaTermino= rs_Horario.getString("Hora_Termino");
                          dia= rs_Horario.getString("Dia_Semana");
                      }
                  }
                  tf_FolioInicio.setText(""+horaInicio);
                  tf_FolioTermino.setText(""+horaTermino);
                  tf_FolioDia.setText(""+dia);
                  

            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
    }
        public void departamento(int id_Departamento){
        String querydepartamento = "SELECT * FROM departamento WHERE id_Departamento = ?";
            try (PreparedStatement pst_departamento = con.prepareStatement(querydepartamento)) {
                System.out.println("Departamento: "+id_Departamento);
                pst_departamento.setInt(1, id_Departamento);  // Establecer el ID del folio en la consulta.
                  try (ResultSet rs_departamento = pst_departamento.executeQuery()) {
                      while (rs_departamento.next()) {  // Iterar sobre los resultados de la consulta.
                          nombreDepartamento= rs_departamento.getString("Nombre");
                      }
                  }
                  if(hasResults){
                      tf_AprobadoDepartamento.setText(nombreDepartamento);
                  }else{
                      tf_EmpleadoDepartamento.setText(nombreDepartamento);
                      
                  }
               
            } catch (SQLException e) {
                System.err.println("Error al cargar las notificaciones: " + e.getMessage());
            }
    }
        
}
