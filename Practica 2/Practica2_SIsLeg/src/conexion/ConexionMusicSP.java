package conexion;
import exceptions.UserIdInUseException;

public class ConexionMusicSP {

	private ConexionWS3270Interface conexion;
	private String ip = "155.210.152.51";
	private String puerto = "3270";
	
	private static ConexionMusicSP instancia = null;
	
	protected ConexionMusicSP(ConexionWS3270Interface comunicacion) {
        this.conexion = comunicacion;
    }

    public static ConexionMusicSP getInstancia(ConexionWS3270Interface comunicacion) {
        if (instancia == null) {
            instancia = new ConexionMusicSP(comunicacion);
        }
        return instancia;
    }
    
	public void conectar() {
		conexion.conectar(ip, puerto);
	}
	
	public boolean login(String usuario, String passwd) throws Exception {
		try {
			System.out.print(conexion.leerPantalla());
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
			return true;
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public void logout() {
		conexion.pulsarTecla(3);
		conexion.enter();
	}
	
	public String verPantalla() {
		return conexion.leerPantalla().toString();
	}
}
