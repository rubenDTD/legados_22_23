package com.example.practica_2.comunication;

import java.io.*;

public class WS3270 {

    protected Process emulator; // proceso del ws3270.exe
    protected InputStream lectura; // entrada de datos
    protected PrintWriter teclado; // salida de datos
    protected String ws3270exe = "C:\\Program Files\\wc3270\\ws3270.exe";
    //protected final String ENTER = "ENTER"; // tecla enter
    private final String FUNCTION_KEY = "PF(%d)"; // tecla F3
    protected static final String OK = "OK";
    protected static final String ASCII = "ascii";
    protected static final String MORE = "More...";
    protected static final String CADENA_CONEXION = "connect %s:%s";

    //private static Comunicacion3270WS instancia = null;

    public WS3270() {
        try {
            this.emulator = Runtime.getRuntime().exec(ws3270exe);
            lectura = this.emulator.getInputStream();
            teclado = new PrintWriter(new OutputStreamWriter(this.emulator.getOutputStream()));
        } catch (FileNotFoundException ef) {
            System.err.println("Error, ejecutable ws3270.exe no encontrado");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error, no se pudo conectar con ws3270.exe");
            System.exit(1);
        }
    }

    /*public static Comunicacion3270WS getInstancia() {
        if (instancia == null) {
            instancia = new Comunicacion3270WS();
        }
        return instancia;
    }*/

    public void escribirLinea(String cadena) throws InterruptedException {
        do {
            cadena += "\n";
            this.teclado.write(cadena);
            this.teclado.flush();
        } while (leerPantalla().toString().contains(OK));
        Thread.sleep(100);

        //Espera una se�al de OK o de MORE...
    }


    public void escribirLineaNoEsperaOK(String cadena) throws InterruptedException {
        cadena += "\n";
        this.teclado.write(cadena);
        this.teclado.flush();
        Thread.sleep(100);
    }

    public void conectar(String ip, String puerto) throws RuntimeException, InterruptedException {
        String cadenaConexion = String.format(CADENA_CONEXION, ip, puerto);
        escribirLinea(cadenaConexion);
        enter();
    }

    public void enter() throws InterruptedException {
        escribirLinea("ENTER");
    }


    public void ascii() throws InterruptedException {
        escribirLineaNoEsperaOK("ascii");
    }


    public StringBuilder leerPantalla() {
        StringBuilder cadena = new StringBuilder();
        try {
            while (lectura.available() == 0); //Espera a que se llene el buffer
            while (lectura.available() > 0) {
                cadena.append((char) lectura.read());
            }
        } catch (IOException ex) {
            cadena = null;
            System.out.println("IOException");
        } finally {
            System.out.println(cadena);
            return cadena;
        }
    }

    public void escribirCadena(String cadena) throws InterruptedException {
        escribirLinea("String(" + cadena + ")");
    }


    public boolean login(String usuario, String contrasena) throws InterruptedException {
        //Escribe el nombre de usuario
        //Si no es v�lido pulsa F3 y Enter para limpiar campos
        //y devolver� FALSE
        escribirCadena(usuario);
        enter();
        //System.out.println(leerPantalla().toString());
        if (buscarCadena("Userid is not authorized")) {
            System.out.println("Userid is not authorized");
            teclaFuncion(3);
            enter();
            return false;
        }
        //Escribe la contrase�a
        //Si no es v�lida pulsa F3 y Enter para limpiar campos
        //y devolver� FALSE
        escribirCadena(contrasena);
        //System.out.println(leerPantalla().toString());
        enter();
        if (buscarCadena("Password incorrect!")) {
            teclaFuncion(3);
            enter();
            return false;
        }
        //Si ya hay un usuario con el mismo ID connectado
        //Lanza una excepci�n
        if (buscarCadena("Userid is in use.")) {
            return false;
        }
        //Si la contrase�a es v�lida devolver� TRUE
        //y ejecutar� el programa
        if (buscarCadena("Press ENTER to continue...")) {
            enter();
            comenzarPrograma();
            return true;
        }
        return false;
    }

    public void teclaFuncion(int teclaF) throws InterruptedException {
        String funcion = String.format(FUNCTION_KEY, teclaF);
        //escribirLineaNoEsperaOK(funcion);
        escribirLinea(funcion);
    }

    public void comenzarPrograma() throws InterruptedException {
        escribirCadena("tareas.c");
        enter();
    }


    public boolean hayMasTexto() throws InterruptedException {
        ascii();
        String cadenaAux = leerPantalla().toString();
        return cadenaAux.contains(MORE);
    }

    public boolean buscarCadena(String cadena) throws InterruptedException {
        ascii();
        return leerPantalla().toString().contains(cadena);
    }

}
