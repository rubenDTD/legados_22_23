package conexion;

public interface ConexionWS3270Interface {
	StringBuilder leerPantalla();
	void escribirLinea(String linea);
	void conectar (String ip, String puerto);
	void enter();
	boolean buscarLinea(String linea);
	void pulsarTecla(int tecla);
	void escribirLineaSinOK(String linea);
	void ascii();
	void escribirCadena(String linea);
}
