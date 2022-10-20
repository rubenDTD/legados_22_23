package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import conexion.ConexionWS3270;
import conexion.MusicSP;
import conexion.TareaEspecifica;
import conexion.TareaGeneral;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListSpecificAction {
	ConexionWS3270 comunicacionWS = ConexionWS3270.getInstancia();
    MusicSP conexion = MusicSP.getInstancia(comunicacionWS);
    
	@FXML
    private Button botonSalir;
	
	@FXML
    TableView<TareaEspecifica> tablaListar;

	@FXML
    TableColumn<TareaEspecifica, String> columnaNumero;
    
    @FXML
    TableColumn<TareaEspecifica, String> columnaNombre;

    @FXML
    TableColumn<TareaGeneral, String> columnaDescripcion;

    @FXML
    TableColumn<TareaGeneral, String> columnaFecha;
    
    @FXML
    private void initialize() {
    	columnaNumero.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumero()));
    	columnaNumero.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNombre()));
        columnaDescripcion.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescripcion()));
        columnaFecha.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFecha()));
    }

    @FXML
    private void botonSalir() {
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
}
