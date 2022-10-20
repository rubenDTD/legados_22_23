package application;

import java.io.IOException;

import conexion.MusicSP;
import conexion.ConexionWS3270;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginAction {
	
	ConexionWS3270 comunicacionWS = ConexionWS3270.getInstancia();
    MusicSP comunicacionSP = MusicSP.getInstancia(comunicacionWS);

	 @FXML
	 private Button botonValidar;

	 @FXML
	 private Button botonSalir;

	 @FXML
	 private TextField campoUser;

	 @FXML
	 PasswordField campoContasena;
	   
	@FXML
	private void botonValidar() {
        try {
            if(campoUser.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                        "Please enter your userID");
                return;
            }
            if(campoContasena.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                        "Please enter your password");
                return;
            }
            boolean exito = comunicacionSP.login(campoUser.getText(), campoContasena.getText());
            if(exito) {
            	Stage ventana = MainApp.primaryStage;
                Scene escena = ventana.getScene();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("Menu.fxml"));
                escena.setRoot(loader.load());
            } else {
            	AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                        "Please enter validate fields");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@FXML
	private void botonSalir() {
		Stage ventana = MainApp.primaryStage;
	    Scene escena = ventana.getScene();
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("Portada.fxml"));
	    try {
	    	escena.setRoot(loader.load());
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
}
