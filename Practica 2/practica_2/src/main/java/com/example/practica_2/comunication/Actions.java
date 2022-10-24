package com.example.practica_2.comunication;

import com.example.practica_2.TareaEspecifica;
import com.example.practica_2.TareaGeneral;

import java.util.ArrayList;

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
    }

    public void logout() throws InterruptedException {
        session.teclaFuncion(3);
        session.escribirCadena("off");
        session.enter();
    }

    // Acciones para tareas.c

    // Añadir nueva tarea general
    public void addTareaGeneral(String descripcion, String dia, String mes) throws InterruptedException {
        System.out.println(descripcion + " " + dia + mes);
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena(dia+mes);
        enterTareas();
        session.escribirCadena(descripcion);
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    public void addTareaEspecifica(String nombre, String descripcion, String dia, String mes) throws InterruptedException {
        System.out.println(nombre + " " + descripcion + " " + dia + mes);
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena("2");
        enterTareas();
        session.escribirCadena(dia+mes);
        enterTareas();
        session.escribirCadena(nombre);
        enterTareas();
        session.escribirCadena(descripcion);
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    public ArrayList<TareaGeneral> getTareasGenerales() throws InterruptedException {
        ArrayList<TareaGeneral> tareas = new ArrayList<>();
        session.escribirCadena("2");
        enterTareas();
        session.escribirCadena("1");
        enterTareas();
        // En este punto se estan viendo las tareas
        session.ascii();
        StringBuilder pantalla = session.leerPantalla();
        System.out.println("Pantalla tareas: " + pantalla.toString());


        return tareas;
    }

    public ArrayList<TareaEspecifica> getTareasEspecificas() throws InterruptedException {
        ArrayList<TareaEspecifica> tareas = new ArrayList<>();
        session.escribirCadena("2");
        enterTareas();
        session.escribirCadena("2");
        enterTareas();
        // En este punto se estan viendo las tareas


        return tareas;
    }

    public void volverAMenu() throws InterruptedException {
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    private void enterTareas() throws InterruptedException {
        session.enter();
        session.hayMasTexto();
    }
}
