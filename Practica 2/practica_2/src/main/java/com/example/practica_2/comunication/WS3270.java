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

    protected static final int delay = 200;

    private static WS3270 session = null;

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

    public static WS3270 getSession() {
        if (session == null) {
            session = new WS3270();
        }
        return session;
    }

    public void escribirLinea(String cadena) throws InterruptedException {
        do {
            cadena += "\n";
            this.teclado.write(cadena);
            this.teclado.flush();
        } while (leerPantalla().toString().contains(OK));
        Thread.sleep(delay);
    }


    public void escribirLineaNoEsperaOK(String cadena) throws InterruptedException {
        cadena += "\n";
        this.teclado.write(cadena);
        this.teclado.flush();
        Thread.sleep(delay);
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


    public int login(String usuario, String pass) throws InterruptedException {
        //Escribir nombre de usuario
        escribirCadena(usuario);
        enter();
        // Si el usuario no es valido pulsa F3 para cancelar
        if (buscarCadena("Userid is not authorized")) {
            System.out.println("Userid is not authorized");
            teclaFuncion(3);
            enter();
            return -1;
        }
        //Escribir pass
        escribirCadena(pass);
        enter();
        // Si pass no es valida pulsa F3 para cancelar
        if (buscarCadena("Password incorrect!")) {
            teclaFuncion(3);
            enter();
            return -2;
        }
        // Si el ID esta en uso se fuerza el acceso
        if (buscarCadena("Userid is in use.")) {
            return -3;
        }

        // Si esta correcto devuelve true
        if (buscarCadena("Press ENTER to continue...")) {
            enter();
            comenzarPrograma();
            return 0;
        }
        // Por defecto devuelve false
        return -10;
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


    public void hayMasTexto() throws InterruptedException {
        ascii();
        String cadenaAux = leerPantalla().toString();
        if(cadenaAux.contains(MORE)) {
            enter();
        }
    }

    public boolean buscarCadena(String cadena) throws InterruptedException {
        ascii();
        return leerPantalla().toString().contains(cadena);
    }

}
