package conexion;
import exceptions.UserIdInUseException;

public class MusicSP {

	private ConexionWS3270Interface conexion;
	private String ip = "155.210.152.51";
	private String puerto = "3270";
	
	private static MusicSP instancia = null;
	
	protected MusicSP(ConexionWS3270Interface comunicacion) {
        this.conexion = comunicacion;
    }

    public static MusicSP getInstancia(ConexionWS3270Interface comunicacion) {
        if (instancia == null) {
            instancia = new MusicSP(comunicacion);
        }
        return instancia;
    }
    
	public void conectar() {
		conexion.conectar(ip, puerto);
	}
	/* Funcionalidades de conexion */
	public boolean login(String usuario, String passwd) throws Exception {
		try {
			conexion.escribirCadena(usuario);
			conexion.enter();
			if (conexion.buscarLinea("Userid is not authorized")) {
				conexion.pulsarTecla(3);
				conexion.enter();
	            return false;
	        }
			conexion.escribirCadena(passwd);
			conexion.enter();
			conexion.enter();
			conexion.ascii();
			if(conexion.buscarLinea("Password incorrect")){
				conexion.pulsarTecla(3);
				conexion.enter();
				return false;
			}
			conexion.ascii();
			if(conexion.buscarLinea("Userid is in use")) {
				throw new UserIdInUseException();
			}
			conexion.ascii();
			conexion.escribirCadena("tareas.c");
			conexion.enter();
			System.out.print(conexion.leerPantalla());
			return true;
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public void logout() {
		conexion.escribirCadena("3"); //Volvemos al Menu
		conexion.enter();
		conexion.enter();
	}
	
	/* Funcionalidades de la App Legada */
	public boolean anadirTareaGeneral(String descripcion, String dia, String mes) {
		System.out.println(descripcion+","+dia+","+mes);
		try {
			conexion.escribirCadena("1"); //Seleccionar 'ASSING TASK'
			conexion.enter();
			conexion.escribirCadena("1"); //Seleccionar 'GENERAL TASK'
			conexion.enter();
			conexion.escribirCadena(dia+mes); //añadimos los datos
			conexion.enter();
			conexion.escribirCadena(descripcion);
			conexion.enter();
			conexion.escribirCadena("3"); //Volvemos al Menu
			conexion.enter();
			return true;
		} catch (Exception e) {
            e.printStackTrace();
            return false;
		}
	}
	
	public boolean anadirTareaEspecifica(String nombre, String descripcion, String dia, String mes) {
		System.out.println(descripcion+","+dia+","+mes);
		try {
			conexion.escribirCadena("1"); //Seleccionar 'ASSING TASK'
			conexion.enter();
			conexion.escribirCadena("2"); //Seleccionar 'SPECIFIC TASK'
			conexion.enter();
			conexion.escribirCadena(dia+mes); //añadimos los datos
			conexion.enter();
			conexion.escribirCadena(nombre);
			conexion.enter();
			conexion.escribirCadena(descripcion);
			conexion.enter();
			conexion.escribirCadena("3"); //Volvemos al Menu
			conexion.enter();
			return true;
		} catch (Exception e) {
            e.printStackTrace();
            return false;
		}
	}
	
	public String verPantalla() {
		return conexion.leerPantalla().toString();
	}
}
