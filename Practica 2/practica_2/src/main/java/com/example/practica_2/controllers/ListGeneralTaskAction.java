package com.example.practica_2.controllers;

import java.io.IOException;

import com.example.practica_2.MainApplication;
import com.example.practica_2.TareaGeneral;
import com.example.practica_2.comunication.Actions;
import com.example.practica_2.comunication.WS3270;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ListGeneralTaskAction {
    Actions acciones = new Actions(WS3270.getSession());

    @FXML
    private Button botonSalir;

    @FXML
    TableView<TareaGeneral> tablaListar;

    @FXML
    TableColumn<TareaGeneral, String> columnaNumero;

    @FXML
    TableColumn<TareaGeneral, String> columnaDescripcion;

    @FXML
    TableColumn<TareaGeneral, String> columnaFecha;

    @FXML
    private void initialize() throws InterruptedException {
        System.out.println("Inicio lista general de tareas");
        tablaListar.getItems().clear();
        tablaListar.setItems(FXCollections.observableArrayList(acciones.getTareasGenerales()));
        columnaNumero.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumero()));
        columnaDescripcion.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescripcion()));
        columnaFecha.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFecha()));
    }

    @FXML
    private void botonSalir() throws InterruptedException {
        acciones.volverAMenu();
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
