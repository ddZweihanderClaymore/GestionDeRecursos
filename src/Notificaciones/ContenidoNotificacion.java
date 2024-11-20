/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Notificaciones;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Clase que representa el contenido de una notificación.
 * Contiene propiedades relacionadas con las notificaciones,
 * incluyendo el ID, el ID del trabajador, la fecha, la descripción y el estado.
 * 
 * @author parca
 */
public class ContenidoNotificacion {

    // Propiedades de la notificación
    private final SimpleIntegerProperty id_Notificacion;  // ID de la notificación
    private final SimpleIntegerProperty id_trabajador;     // ID del trabajador asociado a la notificación
    private final SimpleStringProperty descripcion;         // Descripción de la notificación
    private final SimpleStringProperty fecha;               // Fecha de la notificación
    private final SimpleStringProperty estado;              // Estado de la notificación

    /**
     * Constructor de la clase ContenidoNotificacion.
     *
     * @param id_Notificacion ID de la notificación.
     * @param id_trabajador   ID del trabajador asociado a la notificación.
     * @param fecha           Fecha de la notificación.
     * @param descripcion     Descripción de la notificación.
     * @param estado          Estado de la notificación.
     */
    public ContenidoNotificacion(int id_Notificacion, int id_trabajador, String fecha, String descripcion, String estado) {
        this.id_Notificacion = new SimpleIntegerProperty(id_Notificacion);  // Inicializa el ID de la notificación
        this.id_trabajador = new SimpleIntegerProperty(id_trabajador);      // Inicializa el ID del trabajador
        this.fecha = new SimpleStringProperty(fecha);                        // Inicializa la fecha
        this.descripcion = new SimpleStringProperty(descripcion);            // Inicializa la descripción
        this.estado = new SimpleStringProperty(estado);                      // Inicializa el estado
    }

    /**
     * Obtiene el ID de la notificación.
     *
     * @return El ID de la notificación.
     */
    public int getId_Notificacion() {
        return id_Notificacion.get();  // Retorna el valor del ID de la notificación
    }

    /**
     * Obtiene la propiedad del ID de la notificación.
     *
     * @return La propiedad del ID de la notificación.
     */
    public SimpleIntegerProperty Id_NotificacionProperty() {
        return id_Notificacion;  // Retorna la propiedad del ID de la notificación
    }

    /**
     * Obtiene el ID del trabajador asociado a la notificación.
     *
     * @return El ID del trabajador.
     */
    public int getId_trabajador() {
        return id_trabajador.get();  // Retorna el valor del ID del trabajador
    }

    /**
     * Obtiene la propiedad del ID del trabajador.
     *
     * @return La propiedad del ID del trabajador.
     */
    public SimpleIntegerProperty Id_trabajadorProperty() {
        return id_trabajador;  // Retorna la propiedad del ID del trabajador
    }

    /**
     * Obtiene la descripción de la notificación.
     *
     * @return La descripción de la notificación.
     */
    public String getDescripcion() {
        return descripcion.get();  // Retorna el valor de la descripción
    }

    /**
     * Obtiene la propiedad de la descripción de la notificación.
     *
     * @return La propiedad de la descripción.
     */
    public SimpleStringProperty DescripcionProperty() {
        return descripcion;  // Retorna la propiedad de la descripción
    }

    /**
     * Obtiene la fecha de la notificación.
     *
     * @return La fecha de la notificación.
     */
    public String getFecha() {
        return fecha.get();  // Retorna el valor de la fecha
    }

    /**
     * Obtiene la propiedad de la fecha de la notificación.
     *
     * @return La propiedad de la fecha.
     */
    public SimpleStringProperty FechaProperty() {
        return fecha;  // Retorna la propiedad de la fecha
    }

    /**
     * Obtiene el estado de la notificación.
     *
     * @return El estado de la notificación.
     */
    public String getEstado() {
        return estado.get();  // Retorna el valor del estado
    }

    /**
     * Obtiene la propiedad del estado de la notificación.
     *
     * @return La propiedad del estado.
     */
    public SimpleStringProperty EstadoProperty() {
        return estado;  // Retorna la propiedad del estado
    }
}