package com.example.legados3.Service;

import com.example.legados3.Juego;
import com.example.legados3.OCR.OCR;
import com.example.legados3.Wrapper.Wrapper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wrapper.pulsarTecla('4');
        BufferedImage img = wrapper.capturaPantalla();
        /*File file = new File("FFFFFF.jpg");
        try {
            ImageIO.write(img, "png", file);
            System.out.println("A screenshot is captured to: " + file.getPath());
        } catch(Exception e){
            e.printStackTrace();
        }*/
        String result = ocr.leerImagen(img);
        String[] listWords = result.split(" |\n");
        wrapper.pulsarTecla('\n');
        return listWords[5];
    }

    // Funcion que devuelve la info. de un juego dado su nombre
    public String listDatos(String nombre){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wrapper.pulsarTecla('7');
        wrapper.pulsarTecla('N');
        wrapper.pulsarTecla('\n');
        for (int i=0; i<nombre.length(); i++){
            wrapper.pulsarTecla(nombre.charAt(i));
        }
        wrapper.pulsarTecla('\n');
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage img = wrapper.capturaPantalla();
        String result = ocr.leerImagen(img);
        String[] listWords = result.split("\n");
        for(String u: listWords){
            System.out.println(u);
        }
        if(result.contains("NO HAY NINGUN")){
            wrapper.pulsarTecla('\n');
            wrapper.pulsarTecla('N');
            wrapper.pulsarTecla('\n');
            return "";
        }
        wrapper.pulsarTecla('S');
        wrapper.pulsarTecla('\n');
        wrapper.pulsarTecla('N');
        wrapper.pulsarTecla('\n');
        wrapper.pulsarTecla('N');
        wrapper.pulsarTecla('\n');

        return listWords[0];
    }

    public ArrayList<Juego> list(String cinta){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wrapper.pulsarTecla('6');
        for (int i=0; i<cinta.length(); i++){
            wrapper.pulsarTecla(cinta.charAt(i));
        }
        wrapper.pulsarTecla('\n');
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Juego> listJuegos = new ArrayList<>();
        BufferedImage img = wrapper.capturaPantalla();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = ocr.leerImagen(img);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        img.flush();
        while (!result.contains("M E M U")){
            String[] param; String nombreJuego;
            String[] listWords = result.split("\n");
            for (String a: listWords){
                System.out.println(a);
            }
            System.out.println("----------");
            for (int i=1; i<listWords.length-1; i++) {
                param = listWords[i].split(" ");
                //System.out.println(listWords[i]);
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
            wrapper.pulsarTecla(' ');
            img.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            img = wrapper.capturaPantalla();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = ocr.leerImagen(img);
       }
        return listJuegos;
    }
}
