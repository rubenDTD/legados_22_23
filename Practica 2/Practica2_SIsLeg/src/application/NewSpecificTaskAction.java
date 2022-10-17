package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewSpecificTaskAction {

	@FXML
    private Button botonEnter;

    @FXML
    TextField campoNumero;

    @FXML
    TextField campoNombre;

    @FXML
    TextField campoDescripcion;

    @FXML
    DatePicker campoFecha;

    @FXML
    private void botonEnter() {
    	if(campoNumero.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                    "Please enter one ID");
            return;
        }
        if(campoNombre.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Form Error!", 
                    "Please enter one name");
            return;
        }
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
        final String numero = campoNumero.getText();
        final String nombre = campoNombre.getText();
        final String descripcion = campoDescripcion.getText();
        final LocalDate fecha = campoFecha.getValue();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MM yyyy");
        String fechaAux = df.format(fecha);
        String [] ddmmyy = fechaAux.split(" ");
        try {
           // TODO añadir tarea a ESPECIFICA
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
