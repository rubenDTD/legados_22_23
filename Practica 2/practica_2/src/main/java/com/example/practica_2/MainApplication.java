package com.example.practica_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("conectar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch(); // ESTO EJECUTA LA INTERFAZ DE JAVAFX

        // Comunicacion ws3270exe
        Process emulator;
        String ws3270exe = "C:/Program Files/wc3270/wc3270.exe";
        InputStream lectura = null;
        PrintWriter teclado = null;

        try {
            emulator = Runtime.getRuntime().exec(ws3270exe);
            lectura = emulator.getInputStream();
            teclado = new PrintWriter(new OutputStreamWriter(emulator.getOutputStream()));
        } catch (FileNotFoundException ef) {
            System.err.println("Error, ejecutable WS3270.exe no encontrado");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error, no se pudo conectar con WS3270.exe");
            System.exit(1);
        }

        // Connect IP:PORT
        String cadenaConexion = "Connect(155.210.154.51:3270)";

        do {
            cadenaConexion += "\n";
            teclado.write(cadenaConexion);
            teclado.flush();
        } while (leerPantalla(lectura).toString().contains("OK"));

        // Pulsar ENTER
        String s = "ENTER";
        do {
            s += "\n";
            teclado.write(s);
            teclado.flush();
        } while (leerPantalla(lectura).toString().contains("OK"));






        System.out.println("Fin ejecucion");
    }

    public static StringBuilder leerPantalla(InputStream lectura) {
        StringBuilder cadena = new StringBuilder();
        try {
            while (lectura.available() == 0); //Espera a que se llene el buffer
            while (lectura.available() > 0) {
                cadena.append((char) lectura.read());
            }
        } catch (IOException ex) {
            cadena = null;
        } finally {
            System.out.println(cadena);
            return cadena;
        }
    }
}