package tabla;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class tablaContenido {

    private final SimpleIntegerProperty id_Mobiliario;
    private final SimpleStringProperty Nombre;
    private final SimpleStringProperty Descripcion;
    private final SimpleStringProperty Cantidad; 

    public tablaContenido(int id_Mobiliario, String Nombre, String Descripcion, String Cantidad) {
        this.id_Mobiliario = new SimpleIntegerProperty(id_Mobiliario);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.Descripcion = new SimpleStringProperty(Descripcion);
        this.Cantidad = new SimpleStringProperty(Cantidad);
    }

    public int getId_Mobiliario() {
        return id_Mobiliario.get();
    }

    public SimpleIntegerProperty id_MobiliarioProperty() {
        return id_Mobiliario;
    }

    public String getNombre() {
        return Nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion.get();
    }

    public SimpleStringProperty descripcionProperty() {
        return Descripcion;
    }

    public String getCantidad() { 
        return Cantidad.get();
    }

    public SimpleStringProperty cantidadProperty() { 
        return Cantidad;
    }
}
