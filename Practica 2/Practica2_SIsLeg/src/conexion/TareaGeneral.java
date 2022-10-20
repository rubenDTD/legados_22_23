package conexion;

public class TareaGeneral {

	private final String numero;
    private final String descripcion;
    private final String fecha;

    public TareaGeneral(String numero, String descripcion, String fecha) {
    	this.numero = numero;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
    
    public String getNumero() {
    	return numero;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String toString() {
        return "Numero: "+ numero + ", Descripcion: " + descripcion + ", Fecha: " + fecha;
    }
}
