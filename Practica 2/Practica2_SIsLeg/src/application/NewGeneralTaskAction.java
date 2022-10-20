package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import conexion.ConexionWS3270;
import conexion.MusicSP;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewGeneralTaskAction {

	ConexionWS3270 comunicacionWS = ConexionWS3270.getInstancia();
    MusicSP conexion = MusicSP.getInstancia(comunicacionWS);
    
	@FXML
    private Button botonEnter;

    @FXML
    TextField campoDescripcion;

    @FXML
    DatePicker campoFecha;

    @FXML
    private void botonEnter() {
        if(campoDescripcion.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                    "Please enter one description");
            return;
        }
        IntroducirTarea();
        Stage ventana = MainApp.primaryStage;
        Scene escena = ventana.getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("Menu.fxml"));

        try {
            escena.setRoot(loader.load());
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private void IntroducirTarea() {
        final String descripcion = campoDescripcion.getText();
        final LocalDate fecha = campoFecha.getValue();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MM yyyy");
        String fechaAux = df.format(fecha);
        String [] ddmmyy = fechaAux.split(" ");
        try {
           // Añadir tarea a GENERAL
        	conexion.anadirTareaGeneral(descripcion, ddmmyy[0], ddmmyy[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
