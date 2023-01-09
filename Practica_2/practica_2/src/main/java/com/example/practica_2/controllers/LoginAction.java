package com.example.practica_2.controllers;

import java.io.IOException;

import com.example.practica_2.AlertHelper;
import com.example.practica_2.MainApplication;
import com.example.practica_2.comunication.Actions;
import com.example.practica_2.comunication.WS3270;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginAction {

    Actions acciones = new Actions(WS3270.getSession());

    @FXML
    private Button botonValidar;

    @FXML
    private Button botonSalir;

    @FXML
    private TextField campoUser;

    @FXML
    PasswordField campoPass;

    @FXML
    private void botonValidar() {
        try {
            if(campoUser.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please enter your userID");
                return;
            }
            if(campoPass.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please enter your password");
                return;
            }
            int exito = acciones.login(campoUser.getText(), campoPass.getText());
            if(exito == 0) {
                Stage ventana = MainApplication.primaryStage;
                Scene escena = ventana.getScene();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApplication.class.getResource("Menu.fxml"));
                escena.setRoot(loader.load());
            } else if(exito == -1){
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please enter an valid user");
            } else if(exito == -2){
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please enter a valid pass");
            } else if(exito == -3){
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "UserID is in use");
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Unexpected error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void botonSalir() {
        Stage ventana = MainApplication.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource("Portada.fxml"));
        try {
            escena.setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
