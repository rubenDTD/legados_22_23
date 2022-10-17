package application;

import java.io.IOException;

import conexion.ConexionMusicSP;
import conexion.ConexionWS3270;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuAction {
	
	ConexionWS3270 comunicacionWS = ConexionWS3270.getInstancia();
    ConexionMusicSP comunicacionSP = ConexionMusicSP.getInstancia(comunicacionWS);
	
	@FXML
    private Button botonNewTaskFile;

    @FXML
    private Button botonAddTaskMenu;

    @FXML
    private Button botonAddTask;

    @FXML
    private Button botonSaveTask;

    @FXML
    private Button botonGetTareas;

    @FXML
    private Button botonSearchTask;

    @FXML
    private Button botonLogout;

	@FXML
	private void botonNewGeneralTask() {
		Stage ventana = MainApp.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("NewGeneralTask.fxml"));
        try {
            escena.setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@FXML
	private void botonNewSpecificTask() {
		Stage ventana = MainApp.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("NewSpecificTask.fxml"));
        try {
            escena.setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@FXML
	private void botonGetGeneralTareas() {
	    
	}
	
	@FXML
	private void botonGetSpecificTareas() {
	    
	}
	
	@FXML
	private void botonLogout() {
		comunicacionSP.logout();
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
