package tabla;

import javafx.beans.property.SimpleIntegerProperty; // Importa la clase SimpleIntegerProperty para propiedades enteras
import javafx.beans.property.SimpleStringProperty;  // Importa la clase SimpleStringProperty para propiedades de texto

/**
 * Clase que representa el contenido de una tabla que almacena información sobre mobiliario.
 * Contiene propiedades relacionadas con el ID del mobiliario, nombre, descripción y cantidad.
 */
public class tablaContenido {

    // Propiedades del mobiliario
    private final SimpleIntegerProperty id_Mobiliario;  // ID del mobiliario
    private final SimpleStringProperty Nombre;            // Nombre del mobiliario
    private final SimpleStringProperty Descripcion;       // Descripción del mobiliario
    private final SimpleStringProperty Cantidad;          // Cantidad disponible del mobiliario

    /**
     * Constructor de la clase tablaContenido.
     *
     * @param id_Mobiliario ID del mobiliario.
     * @param Nombre        Nombre del mobiliario.
     * @param Descripcion   Descripción del mobiliario.
     * @param Cantidad      Cantidad disponible del mobiliario.
     */
    public tablaContenido(int id_Mobiliario, String Nombre, String Descripcion, String Cantidad) {
        this.id_Mobiliario = new SimpleIntegerProperty(id_Mobiliario);  // Inicializa el ID del mobiliario
        this.Nombre = new SimpleStringProperty(Nombre);                  // Inicializa el nombre del mobiliario
        this.Descripcion = new SimpleStringProperty(Descripcion);        // Inicializa la descripción del mobiliario
        this.Cantidad = new SimpleStringProperty(Cantidad);              // Inicializa la cantidad disponible
    }

    /**
     * Obtiene el ID del mobiliario.
     *
     * @return El ID del mobiliario.
     */
    public int getId_Mobiliario() {
        return id_Mobiliario.get();  // Retorna el valor del ID del mobiliario
    }

    /**
     * Obtiene la propiedad del ID del mobiliario.
     *
     * @return La propiedad del ID del mobiliario.
     */
    public SimpleIntegerProperty id_MobiliarioProperty() {
        return id_Mobiliario;  // Retorna la propiedad del ID del mobiliario
    }

    /**
     * Obtiene el nombre del mobiliario.
     *
     * @return El nombre del mobiliario.
     */
    public String getNombre() {
        return Nombre.get();  // Retorna el valor del nombre
    }

    /**
     * Obtiene la propiedad del nombre del mobiliario.
     *
     * @return La propiedad del nombre.
     */
    public SimpleStringProperty nombreProperty() {
        return Nombre;  // Retorna la propiedad del nombre
    }

    /**
     * Obtiene la descripción del mobiliario.
     *
     * @return La descripción del mobiliario.
     */
    public String getDescripcion() {
        return Descripcion.get();  // Retorna el valor de la descripción
    }

    /**
     * Obtiene la propiedad de la descripción del mobiliario.
     *
     * @return La propiedad de la descripción.
     */
    public SimpleStringProperty descripcionProperty() {
        return Descripcion;  // Retorna la propiedad de la descripción
    }

    /**
     * Obtiene la cantidad disponible del mobiliario.
     *
     * @return La cantidad disponible.
     */
    public String getCantidad() { 
        return Cantidad.get();  // Retorna el valor de la cantidad
    }

    /**
     * Obtiene la propiedad de la cantidad disponible del mobiliario.
     *
     * @return La propiedad de la cantidad.
     */
    public SimpleStringProperty cantidadProperty() { 
        return Cantidad;  // Retorna la propiedad de la cantidad
    }
}