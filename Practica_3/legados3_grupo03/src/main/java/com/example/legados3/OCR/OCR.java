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

    public void cambiarLang(int num){
        String data = "spa";
        ocr.setLanguage(data+Integer.toString(num));
    }

    // Transforma una captura de pantalla en un string
    public String leerImagen(BufferedImage imagen){
        //ocr.setLanguage("spa2");
        String result = "";
        try {
            result = ocr.doOCR(imagen);
        } catch (TesseractException e){
            e.printStackTrace();
        }
        return result;
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int calculate(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        int substitution = calculate(x.substring(1), y.substring(1))
                + costOfSubstitution(x.charAt(0), y.charAt(0));
        int insertion = calculate(x, y.substring(1)) + 1;
        int deletion = calculate(x.substring(1), y) + 1;

        return min(substitution, insertion, deletion);
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}
