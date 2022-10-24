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


    public ArrayList<TareaEspecifica> getTareasEspecificas() {
		try {
			escribirCadena("2");
			enter();
			escribirCadena("2");
			enter();
			ascii();
			String s = leerPantalla().toString();
			escribirCadena("3");
			enter();
			ArrayList<TareaEspecifica> tareas = new ArrayList<TareaEspecifica>(); 
			int start = s.indexOf("data: TASK ");		
			int end = s.indexOf("\r\n",start);
			int next = s.indexOf("data: TASK ",end);
			while(next != -1) {
				if((s.indexOf(" SPECIFIC",start) - start) < 17) {
					end = s.indexOf("\r\n",start);
					String sAux;
					if(end != -1) {sAux = s.substring(start,end);}
					else {sAux = s.substring(start,s.indexOf("data: ",start+1));}
					System.out.println(sAux);
					int initFecha = (sAux.indexOf("SPECIFIC ")) + 9;
					int initNom = initFecha + 5;												
					int initDesc = (sAux.indexOf(" ",initNom)) + 1;																
					String fecha = sAux.substring(initFecha,initFecha + 4);
					int finDesc = initDesc;
					while(finDesc < sAux.length() && Character.isLetter(sAux.charAt(finDesc))) {
						finDesc++;
					}
					String nombre = sAux.substring(initNom,initDesc-1);							
					String desc = sAux.substring(initDesc,finDesc);												
					//System.out.println(fecha+" "+desc+" "+nombre+"|||");
					String num = sAux.substring(11,sAux.indexOf(":",11));
					TareaEspecifica t = new TareaEspecifica(num,nombre,desc,fecha);
					tareas.add(t);
				}
				start = next;
				next = s.indexOf("data: TASK ",start+1);
				if(next == -1 && s.substring(start,start+15).contains("data: TASK ")) {
					if((s.indexOf(" SPECIFIC",start) - start) < 17) {
						end = s.indexOf("\r\n",start);
						String sAux;
						if(end != -1) {sAux = s.substring(start,end);}
						else {sAux = s.substring(start,s.indexOf("data: ",start+1));}
						System.out.println(sAux);
						int initFecha = (sAux.indexOf("SPECIFIC ")) + 9;
						int initNom = initFecha + 5;												
						int initDesc = (sAux.indexOf(" ",initNom)) + 1;																
						String fecha = sAux.substring(initFecha,initFecha + 4);
						int finDesc = initDesc;
						while(finDesc < sAux.length() && Character.isLetter(sAux.charAt(finDesc))) {
							finDesc++;
						}
						String nombre = sAux.substring(initNom,initDesc-1);							
						String desc = sAux.substring(initDesc,finDesc);												
						//System.out.println(fecha+" "+desc+" "+nombre+"|||");
						String num = sAux.substring(11,sAux.indexOf(":",11));
						TareaEspecifica t = new TareaEspecifica(num,nombre,desc,fecha);
						tareas.add(t);
					}
				}
			}			
			return tareas;
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}
