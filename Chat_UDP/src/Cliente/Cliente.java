package Cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Cliente {
	//
	public static void main(String[] args) {
		Ventana v=new Ventana(JOptionPane.showInputDialog("¿Cuál es tu nickname?", null));
	}//MAIN
	public void Sape() {
		//v.run();
	}
	
}//CLASS
