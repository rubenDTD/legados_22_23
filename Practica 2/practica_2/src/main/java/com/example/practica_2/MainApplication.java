package com.example.practica_2;

import com.example.practica_2.comunication.WS3270;
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

    public static void main(String[] args) throws InterruptedException {
        //launch(); // ESTO EJECUTA LA INTERFAZ DE JAVAFX
        String ip = "155.210.152.51";
        String puerto = "3270";
        // Ejeuctar programa
        WS3270 comunicacion = new WS3270();

        // Conectar: comando + enter
        comunicacion.conectar(ip, puerto);
        comunicacion.enter();

        // Escribir usuario
        comunicacion.escribirCadena("grupo_03");
        comunicacion.enter();

        // Escribir pass
        comunicacion.escribirCadena("secreto6");
        comunicacion.enter();

        // Acceder a aplicacion
        comunicacion.enter();
        comunicacion.ascii();
        comunicacion.enter();





        System.out.println("Fin ejecucion");
    }
}
