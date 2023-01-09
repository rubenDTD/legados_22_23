package com.example.practica_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class MainApplication extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Portada.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        String titulo = "Wrapper 3270";
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(); // ESTO EJECUTA LA INTERFAZ DE JAVAFX

        System.out.println("Fin ejecucion");
    }
}
