package com.example.legados3.Wrapper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

// Simula el un Wrapper de la aplicacion legada MS-DOS
public class Wrapper {

    private Robot robot;

    public Wrapper() throws AWTException {
        System.setProperty("java.awt.headless", "false");
        robot = new Robot();
        robot.setAutoDelay(20);
        robot.waitForIdle();
        System.setProperty("java.awt.headless", "true");
    }

    //Pulsa la tecla del caracter pasado como parametro
    public void pulsarTecla(char caracter){
        System.setProperty("java.awt.headless", "false");
        try {
            Thread.sleep(200);
            if(Character.isUpperCase(caracter)){ robot.keyPress(KeyEvent.VK_SHIFT); }
            robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(caracter));
            robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(caracter));
            if(Character.isUpperCase(caracter)){ robot.keyRelease(KeyEvent.VK_SHIFT); }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.setProperty("java.awt.headless", "true");
    }

    // Captura la pantalla
    public BufferedImage capturaPantalla(){
        System.setProperty("java.awt.headless", "false");
        BufferedImage i = robot.createScreenCapture(new Rectangle(635, 130, 650, 400));
        System.setProperty("java.awt.headless", "true");
        return i;
    }

    // Mueve el cursor para abrir y cerrar la aplicacion legada
    public void moveNavigateBar(){
        System.setProperty("java.awt.headless", "false");
        try {
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
        robot.mouseMove(520, 700);
        try {
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        System.setProperty("java.awt.headless", "true");
    }

}
