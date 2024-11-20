/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Notificaciones;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author parca
 */
public class ContenidoNotificacion {

    private final SimpleIntegerProperty id_Notificacion;
    private final SimpleIntegerProperty id_trabajador;
    private final SimpleStringProperty descripcion;
    private final SimpleStringProperty fecha;
    private final SimpleStringProperty estado;
    public ContenidoNotificacion(int id_Notificacion,int id_trabajador,  String fecha, String descripcion, String estado) {
        this.id_Notificacion = new SimpleIntegerProperty(id_Notificacion);
        this.id_trabajador = new SimpleIntegerProperty(id_trabajador);
        this.fecha = new SimpleStringProperty(fecha);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId_Notificacion() {
        return id_Notificacion.get();
    }

    public SimpleIntegerProperty Id_NotificacionProperty() {
        return id_Notificacion;
    }

        public int getId_trabajador() {
        return id_trabajador.get();
    }

    public SimpleIntegerProperty Id_trabajadorProperty() {
        return id_trabajador;
    }
    
    public String getDescripcion() {
        return descripcion.get();
    }

    public SimpleStringProperty DescripcionProperty() {
        return descripcion;
    }

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty FechaProperty() {
        return fecha;
    }

    public String getEstado() {
        return estado.get();
    }

    public SimpleStringProperty EstadoProperty() {
        return estado;
    }
}
