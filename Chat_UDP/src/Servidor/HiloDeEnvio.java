package Servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class HiloDeEnvio extends Thread {
	int puertodondemandarecosas=0;
	InetAddress direcciondondemandarecosas=null;
	DatagramSocket socket=null;
	Pausa p;
	public HiloDeEnvio(int iimidireccion, DatagramPacket iirecibo, Pausa iip) {
		try {
			socket=new DatagramSocket(iimidireccion);
			direcciondondemandarecosas=iirecibo.getAddress();
			puertodondemandarecosas=iirecibo.getPort();
			p=iip;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
	}
	public void run() {
		while(true) {
			Pausarme();
			System.out.println("Me han despausado");
			Enviar();
		}
	}//RUN
	public void Enviar() {
		byte[] enviados= new byte[1024];		
		enviados=Servidor.Get_LastLog().getBytes();
		DatagramPacket envio = new DatagramPacket(enviados,enviados.length,direcciondondemandarecosas,puertodondemandarecosas);
		try {
			socket.send(envio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
	}

	public void Pausarme() {
		try {
			synchronized(p) {
				p.wait();
			}		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}//CLASS
