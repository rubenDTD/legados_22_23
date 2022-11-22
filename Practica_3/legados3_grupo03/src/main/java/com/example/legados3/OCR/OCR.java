package com.example.legados3.OCR;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.util.Arrays;

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
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}
