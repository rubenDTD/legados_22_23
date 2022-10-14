package com.example.practica_2.controllers;

import com.example.practica_2.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConectarController {

    public Label connectText;
    @FXML
    public void botonConectar() {
        connectText.setText("Connecting...!");
    }
}
