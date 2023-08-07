package Servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Servidor {	
	static DatagramSocket socket=null;
	static DatagramPacket recibo=null;
	static int puertodondemandarecosas=0;
	static int nuevadireccion=12345; 
	static ArrayList<HiloDeRecepcion> hilosR=new ArrayList<HiloDeRecepcion>();
	static Pausa p=new Pausa();
	static ArrayList<String> log=new ArrayList<String>();
	static int contador=0;
	
	public static void main(String[] args) throws InterruptedException {	
		try {
			socket = new DatagramSocket(12345);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(contador<3) {//Maximo 3 clientes y ya deja de establecer conexiones
			Establecer_ConexionCliente();
			contador++;
		}
	}//MAIN
	public static void Establecer_ConexionCliente() throws InterruptedException {
		try {
			byte[] buffer =new byte[1024];			
			recibo = new DatagramPacket(buffer,buffer.length);
			socket.receive(recibo);
			String mensaje = new String(recibo.getData()).trim();
			System.out.println("Servidor recibe:"+ mensaje);
			log.add(mensaje);
			Redirigir_Mensajes();		
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
	}//ESTABLECER CONEXION

	public static void Lanzar_HiloRecibo(int a) {
		HiloDeRecepcion hdr=new HiloDeRecepcion(a, recibo, p);
		hilosR.add(hdr);
		hilosR.get(hilosR.size()-1).start();
	}
	public static void Lanzar_HiloDeEnvio(int a) {
		HiloDeEnvio hde=new HiloDeEnvio(a, recibo, p);
		hde.start();
	}
	public static void Redirigir_Mensajes() {
		nuevadireccion+=1;
		Lanzar_HiloRecibo(nuevadireccion);
		Lanzar_HiloDeEnvio(nuevadireccion+3);//Me puedo permitir este salto ya que solo permito 3 chats, en caso de tener mas tendria que aumentarlo una mas para que el calculo cuadre (esto es un dolor)
		
	}//REDIRIGIRMENSAJES
	
	public static void Set_Log(String s) {
		log.add(s);
	}

	public static String Get_LastLog() {
		return log.get(log.size()-1);
	}

	public static void Despausar() {
		synchronized(p) {
			p.notify();
		}
		
	}
}//CLASS
