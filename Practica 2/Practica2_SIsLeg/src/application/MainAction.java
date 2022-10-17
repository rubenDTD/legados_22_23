package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainAction {

	@FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    
	@FXML
    private Button submitButton;
	

	@FXML
    protected void botonConectar(ActionEvent event) {
		Stage ventana = MainApp.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("Login.fxml"));

        try {
            escena.setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
