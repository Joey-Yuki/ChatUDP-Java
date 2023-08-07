package Servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class HiloDeRecepcion extends Thread {
	int puertodesdedondevoyarecibir=0;
	InetAddress direcciondondemandarecosas=null;
	DatagramSocket socket=null;
	DatagramPacket recibo=null;
	Pausa p=null;
	
	public HiloDeRecepcion(int iimidireccion, DatagramPacket iirecibo, Pausa iip) {
		puertodesdedondevoyarecibir=iirecibo.getPort();
		direcciondondemandarecosas=iirecibo.getAddress();
		p=iip;
		try {
			socket=new DatagramSocket(iimidireccion);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
	}//CONSTRUCTOR
	
	@Override
	public void run() {
		System.out.println("Me han lanzado");
		Redireccionar();
		while(true) {
			Leer();
		}
	}//RUN
	
	public void Redireccionar() {
		String cadena="";
		byte[] enviados= new byte[1024];		
		enviados=cadena.getBytes();		
		DatagramPacket envio = new DatagramPacket(enviados,enviados.length,direcciondondemandarecosas,puertodesdedondevoyarecibir);
		try {
			socket.send(envio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//REDIRECCIONAR

	public void Leer() {
		try {
			byte[] buffer =new byte[1024];		
			recibo = new DatagramPacket(buffer,buffer.length);
			socket.receive(recibo);
			//System.out.println(new String(recibo.getData()));
			Servidor.Set_Log(new String(recibo.getData()));	
			Despausar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//LEER

	public void Despausar() {
		synchronized(p) {
			p.notifyAll();
		}
	}
}//CLASS
