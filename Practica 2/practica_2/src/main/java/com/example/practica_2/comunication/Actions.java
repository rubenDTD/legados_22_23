package com.example.practica_2.comunication;

import com.example.practica_2.TareaEspecifica;
import com.example.practica_2.TareaGeneral;

import java.util.ArrayList;

public class Actions {

    private WS3270 session;
    private static String ip = "155.210.152.51";
    private static String puerto = "3270";


    // Constructor
    public Actions(WS3270 session) {
        this.session = session;
    }

    // Acciones para acceder a MUSIC/SP
    // Conexion con MUSIC/SP
    public void conectar() throws InterruptedException {
        session.conectar(ip, puerto);
        System.out.println("conectado");
        session.enter();
        System.out.println("enter");
    }

    // Introducir datos de usuario
    public int login(String usuario, String pass) throws InterruptedException {

        return session.login(usuario,pass);
    }

    public void logout() throws InterruptedException {
        session.teclaFuncion(3);
        session.escribirCadena("off");
        session.enter();
    }

    // Acciones para tareas.c

    // Añadir nueva tarea general
    public void addTareaGeneral(String descripcion, String dia, String mes) throws InterruptedException {
        System.out.println(descripcion + " " + dia + mes);
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena(dia+mes);
        enterTareas();
        session.escribirCadena(descripcion);
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    public void addTareaEspecifica(String nombre, String descripcion, String dia, String mes) throws InterruptedException {
        System.out.println(nombre + " " + descripcion + " " + dia + mes);
        session.escribirCadena("1");
        enterTareas();
        session.escribirCadena("2");
        enterTareas();
        session.escribirCadena(dia+mes);
        enterTareas();
        session.escribirCadena(nombre);
        enterTareas();
        session.escribirCadena(descripcion);
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    public void volverAMenu() throws InterruptedException {
        enterTareas();
        session.escribirCadena("3");
        enterTareas();
    }

    private void enterTareas() throws InterruptedException {
        session.enter();
        session.hayMasTexto();
    }



    public ArrayList<TareaGeneral> getTareasGenerales() {
		try {
			session.escribirCadena("2");
			session.enter();
			session.escribirCadena("1");
			session.enter();
			session.ascii();
			String s = session.leerPantalla().toString();
			if(s.contains("More...")) {
	            session.enter();
	        }
			session.escribirCadena("3");
			session.enter();
			ArrayList<TareaGeneral> tareas = new ArrayList<TareaGeneral>();
			int start = s.indexOf("data: TASK ");
			int end = s.indexOf("\r\n",start);
			int next = s.indexOf("data: TASK ",end);	
			while(start != -1) {
				System.out.println("while");
				if((s.indexOf(" GENERAL",start) - start) < 17) {
					System.out.println("if");
					end = s.indexOf("\r\n",start);
					String sAux;
					if(end != -1) {sAux = s.substring(start,end);}
					else {sAux = s.substring(start,s.indexOf("data: ",start+1));}
					System.out.println(sAux);
					int initFecha = (sAux.indexOf("GENERAL ")) + 8;
					int initDesc = sAux.indexOf("----- ",initFecha) + 6;
					String fecha = sAux.substring(initFecha,initFecha + 4);
					int finDesc = initDesc;
					while(finDesc < sAux.length() && Character.isLetter(sAux.charAt(finDesc))) {
						finDesc++;
					}
					String desc = sAux.substring(initDesc,finDesc);
					String num = sAux.substring(11,sAux.indexOf(":",11));
					TareaGeneral t = new TareaGeneral(num,desc,fecha);
					tareas.add(t);
				}
				start = next;
				next = s.indexOf("data: TASK ",start+1);				
			}
			return tareas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    public ArrayList<TareaEspecifica> getTareasEspecificas() {
		try {
			session.escribirCadena("2");
			enterTareas();
			session.escribirCadena("2");
			enterTareas();
			session.ascii();
			String s = session.leerPantalla().toString();
			if(s.contains("More...")) {
	            session.enter();
	        }
			session.escribirCadena("3");
			session.enter();
			ArrayList<TareaEspecifica> tareas = new ArrayList<TareaEspecifica>(); 
			int start = s.indexOf("data: TASK ");		
			int end = s.indexOf("\r\n",start);
			int next = s.indexOf("data: TASK ",end);
			while(start != -1) {
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
			}
			return tareas;
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}
