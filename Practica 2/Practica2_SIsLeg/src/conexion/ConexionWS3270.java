package conexion;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ConexionWS3270 implements ConexionWS3270Interface {

	/* Cambiar por el PATH donde se encuentre el ejecutable ws3270.exe */
	private static String PATH = "C:\\Users\\David\\Desktop\\";
	
	protected Process emulador; // proceso del ws3270.exe
    protected InputStream lectura; // entrada de datos
    protected PrintWriter escritura; // salida de datos
    
	private static String ws3270exe = "ws3270.exe";
	
	 private static ConexionWS3270 instancia = null;
	
	public ConexionWS3270() {
		try {
			this.emulador = Runtime.getRuntime().exec(PATH + ws3270exe);
			lectura = this.emulador.getInputStream();
			escritura = new PrintWriter(new OutputStreamWriter(emulador.getOutputStream()));
		} catch (FileNotFoundException ef) {
            System.err.println("Error, ejecutable ws3270.exe no encontrado");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error, no se pudo conectar con ws3270.exe");
            System.exit(1);
        }
	}
	
	 public static ConexionWS3270 getInstancia() {
	        if (instancia == null) {
	            instancia = new ConexionWS3270();
	        }
	        return instancia;
	    }
	
	@Override
	public StringBuilder leerPantalla() {
		ascii();
		StringBuilder buffer = new StringBuilder();
		try {
			while (lectura.available()==0);
			while (lectura.available() > 0) {
				buffer.append((char) lectura.read());
            }
			
		} catch (IOException ex){
			buffer = null;
		}
		return buffer;
	}
	
	@Override
	public void escribirCadena(String linea) {
		escribirLinea("string "+linea);
	}
	
	@Override
	public void escribirLinea(String linea) {
		do {
			linea = linea + "\n";
			this.escritura.write(linea);
			this.escritura.flush();
			
		} while (leerPantalla().toString().contains("OK"));
	}
	
	@Override
	public void escribirLineaSinOK(String linea) {
		linea = linea + "\n";
		this.escritura.write(linea);
		this.escritura.flush();
	}
	
	@Override
	public void enter() {
		escribirLinea("ENTER");
	}
	
	@Override
    public void ascii() {
		escribirLineaSinOK("ascii");
    }
	
	@Override
	public void pulsarTecla(int tecla) {
		escribirLineaSinOK(String.format("PF(%d)", tecla));
	}
	
	@Override
	public boolean buscarLinea(String linea) {
		ascii();
	    return leerPantalla().toString().contains(linea);
	}

	@Override
	public void conectar (String ip, String puerto) {
		String conexion = "connect "+ip+":"+puerto;
		escribirLinea(conexion);
		escribirLinea("ENTER");
	}
}
