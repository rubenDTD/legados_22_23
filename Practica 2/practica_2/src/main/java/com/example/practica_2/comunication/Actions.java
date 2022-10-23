package com.example.practica_2.comunication;

public class Actions {

    private WS3270 session;
    private static String ip = "155.210.152.51";
    private static String puerto = "3270";


    // Constructor
    public Actions(WS3270 session) {
        this.session = session;
    }

    // Acciones para acceder a MUSIC/SP
    // Conexion con MUSIC/SP
    public void conectar() throws InterruptedException {
        session.conectar(ip, puerto);
        System.out.println("conectado");
        session.enter();
        System.out.println("enter");
    }

    // Introducir datos de usuario
    public int login(String usuario, String pass) throws InterruptedException {

        return session.login(usuario,pass);

        /*// Escribir usuario
        session.escribirCadena(usuario);
        session.enter();

        // Escribir pass
        session.escribirCadena(pass);
        session.enter();

        // Acceder a aplicacion
        session.enter();

        // Introducir 'tareas.c'
        session.comenzarPrograma();*/
    }

    public void logout() throws InterruptedException {
        session.teclaFuncion(3);
        session.escribirCadena("off");
        session.enter();
    }

    // Acciones para tareas.c



}
