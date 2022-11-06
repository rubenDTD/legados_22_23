package com.example.legados3.Wrapper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

// Simula el un Wrapper de la aplicacion legada MS-DOS
public class Wrapper {

    private Robot robot;

    public Wrapper() throws AWTException {
        robot = new Robot();
        robot.setAutoDelay(20);
        robot.waitForIdle();
    }

    //Pulsa la tecla del caracter pasado como parametro
    public void pulsarTecla(char caracter){
        try {
            Thread.sleep(200);
            if(Character.isUpperCase(caracter)){ robot.keyPress(KeyEvent.VK_SHIFT); }
            robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(caracter));
            robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(caracter));
            if(Character.isUpperCase(caracter)){ robot.keyRelease(KeyEvent.VK_SHIFT); }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Captura la pantalla
    public BufferedImage capturaPantalla(){
        return robot.createScreenCapture(new Rectangle(635, 130, 650, 400));
    }
}
