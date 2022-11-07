package com.example.legados3.Service;

import com.example.legados3.Juego;
import com.example.legados3.OCR.OCR;
import com.example.legados3.Wrapper.Wrapper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

@Service
public class WebService {

    private Wrapper wrapper;
    private OCR ocr;

    public WebService() throws AWTException {
        wrapper = new Wrapper();
        ocr = new OCR();
    }

    // Funcion que devuelve el numero de juegos totales que hay en el sistema
    public String totalGames(){
        wrapper.moveNavigateBar();  // Movemos el raton y hacemos click para que se abra el programa
        wrapper.pulsarTecla('4'); // Selecionamos la opcion que muestra el total de juegos
        BufferedImage img = wrapper.capturaPantalla(); // Capturamos la pantalla
        String result = ocr.leerImagen(img);
        String[] listWords = result.split(" |\n"); // Leemos el dato que queremos
        wrapper.pulsarTecla('\n'); // Volvemos a la pantalla de MENU
        wrapper.moveNavigateBar();
        return listWords[5];
    }

    // Funcion que devuelve la info. de un juego dado su nombre
    public String listDatos(String nombre){
        wrapper.moveNavigateBar(); // Movemos el raton y hacemos click para que se abra el programa
        wrapper.pulsarTecla('7'); // Seleccionamos la opcion de buscar un juego especifico
        wrapper.pulsarTecla('N'); // por su nombre
        wrapper.pulsarTecla('\n');
        for (int i=0; i<nombre.length(); i++){  // Escribimos el nombre por pantalla
            wrapper.pulsarTecla(nombre.charAt(i));
        }
        wrapper.pulsarTecla('\n');
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage img = wrapper.capturaPantalla(); // Capturamos la pantalla
        String result = ocr.leerImagen(img);
        String[] listWords = result.split("\n");
        if(result.contains("NO HAY NINGUN")){ // Si no hay ningun resultado
            wrapper.pulsarTecla('\n'); // devulvemos una cadena vacia
            wrapper.pulsarTecla('N');
            wrapper.pulsarTecla('\n');
            return "";
        }
        wrapper.pulsarTecla('S'); // Indicamos que el juego encontrado es el indicado
        wrapper.pulsarTecla('\n');
        wrapper.pulsarTecla('N'); // Indicamos que ya no queremos alternar los datos
        wrapper.pulsarTecla('\n');
        wrapper.pulsarTecla('N'); // Indicamis que ya no queremos buscar mas
        wrapper.pulsarTecla('\n');
        wrapper.moveNavigateBar(); // Ceramos la aplicacion legada
        return listWords[0];
    }

    // Funcion que devuelve la lista de juegos dado el numero de cinta
    public ArrayList<Juego> list(String cinta){
        wrapper.moveNavigateBar(); // Movemos el raton y hacemos click para que se abra el programa
        wrapper.pulsarTecla('6'); // Seleccionamos la opcion de listar los juegos
        BufferedImage img = wrapper.capturaPantalla();
        String result = ocr.leerImagen(img);
        if (result.contains("AMTIGUEDAD")){ // Ordenamos los juegos por el numero de cinta, si
            wrapper.pulsarTecla('\n');  // no lo estan ya
            wrapper.pulsarTecla('\n');
            wrapper.pulsarTecla('3');
            wrapper.pulsarTecla('3');
            wrapper.pulsarTecla('\n');
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wrapper.pulsarTecla('\n');
            wrapper.pulsarTecla('6');
        }
        for (int i=0; i<cinta.length(); i++){   //Escribimos numero de la cintas a buscar
            wrapper.pulsarTecla(cinta.charAt(i));
        }
        wrapper.pulsarTecla('\n'); // Esperamos a que carge la aplicacion legada
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Juego> listJuegos = new ArrayList<>(); // Capturamos la primera pantalla
        img = wrapper.capturaPantalla();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = ocr.leerImagen(img);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        img.flush();
        while (!result.contains("B K ACABAR")){ //Mientras que no se vuelva a la pantalla del menu
            String[] param; String nombreJuego;
            String[] listWords = result.split("\n"); // Guardamos los resultados en un array de juegos
            for (int i=1; i<listWords.length-1; i++) {
                param = listWords[i].split(" ");
                if (param.length >= 5) {
                    nombreJuego = "";
                    for (int x = 1; x < param.length - 3; x++) {
                        nombreJuego += " " + param[x];
                    }
                    Juego nuevo = new Juego(param[0], nombreJuego, param[param.length - 3],
                            param[param.length - 2], param[param.length - 1]);
                    listJuegos.add(nuevo);
                }
            }
            wrapper.pulsarTecla(' '); //Pulsamos ' ' para pasar la pagina de la lista
            img.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            img = wrapper.capturaPantalla(); // Volvemos a capturar la pantalla
            try {                           // y comprobamos que no es el MENU
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = ocr.leerImagen(img);
       }
        wrapper.moveNavigateBar(); // Quitamos la aplicacion legada de la pantalla
        return listJuegos;
    }
}
