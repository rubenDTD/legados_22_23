import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Prueba {
	
    private static String ip = "155.210.152.51";
    private static String puerto = "3270";

	public static void main(String[] args) {
		Comunicacion3270WS comunicacion = new Comunicacion3270WS();
		comunicacion.conectar(ip, puerto);
		comunicacion.enter();
		comunicacion.escribirCadena("grupo_03");
		comunicacion.enter();
		//comunicacion.ascii();
		//System.out.println(comunicacion.leerPantalla().toString());
		comunicacion.escribirCadena("secreto6");
		comunicacion.enter();
		comunicacion.enter();
		comunicacion.ascii();
		System.out.println(comunicacion.leerPantalla().toString());		    
	}

}