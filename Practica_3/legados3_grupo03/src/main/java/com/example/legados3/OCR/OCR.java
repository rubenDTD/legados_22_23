package com.example.legados3.OCR;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

/* Modulo OCR que sirve para leer caracteres de una imagen */
public class OCR {

    private Tesseract1 ocr;
    public OCR(){
        ocr = new Tesseract1();
        ocr.setDatapath("src/main/resources/tessdata");
    }

    // Transforma una captura de pantalla en un string
    public String leerImagen(BufferedImage imagen){
        ocr.setLanguage("spa2");
        String result = "";
        try {
            result = ocr.doOCR(imagen);
        } catch (TesseractException e){
            e.printStackTrace();
        }
        return result;
    }
}
