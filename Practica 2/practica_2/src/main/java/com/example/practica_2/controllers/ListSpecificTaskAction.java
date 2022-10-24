package com.example.practica_2.controllers;

import java.io.IOException;

import com.example.practica_2.MainApplication;
import com.example.practica_2.TareaEspecifica;
import com.example.practica_2.comunication.Actions;
import com.example.practica_2.comunication.WS3270;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ListSpecificTaskAction {
    Actions acciones = new Actions(WS3270.getSession());

    @FXML
    private Button botonSalir;

    @FXML
    TableView<TareaEspecifica> tablaListar;

    @FXML
    TableColumn<TareaEspecifica, String> columnaNumero;

    @FXML
    TableColumn<TareaEspecifica, String> columnaNombre;

    @FXML
    TableColumn<TareaEspecifica, String> columnaDescripcion;

    @FXML
    TableColumn<TareaEspecifica, String> columnaFecha;

    @FXML
    private void initialize() {
        columnaNumero.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumero()));
        columnaNumero.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNombre()));
        columnaDescripcion.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescripcion()));
        columnaFecha.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFecha()));
    }

    @FXML
    private void botonSalir() {
        Stage ventana = MainApplication.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource("Menu.fxml"));

        try {
            escena.setRoot(loader.load());
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
