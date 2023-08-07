package Cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Ventana extends JFrame implements Runnable, ActionListener {
	
	JButton b;
	JTextField txt;
	static String nickname;
	static JTextArea txts=HacerArea();
	DatagramSocket clientSocket=null;
	InetAddress IPservidor=null;
	int puerto=12345;
	DatagramPacket recibo=null;
	@Override
	public void run() {
		Contactar_Servidor();
		while(true) {
			LeerMensajes();
		}//WHILE
	}//RUN
	public Ventana(String nickname) {
		this.nickname=nickname;
		setSize(600,700);
		setLayout(null);
		setName(nickname);
		this.LayoutBoton();
		this.LayoutTextBox();
		this.LayoutCajamensajes();
		add(txts);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		run();
	}//CONSTRUCTOR
	@Override
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void LayoutBoton() {
		b=new JButton("click");
		b.setBounds(430,60,100, 40);
		b.setText("Dale");
		b.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				Enviar_Mensaje();
				txt.setText("");
			}//ACTIONPERFORMED
		});
		add(b);
	}

	public void LayoutTextBox() {
		txt=new JTextField();		
		txt.setBounds(20, 60, 400, 40);	
		txt.setVisible(true);
		add(txt);
	}//LAYOUT
	
	public void LayoutCajamensajes() {
		JTextArea cajamensajes=new JTextArea();
		cajamensajes.setBounds(20,110, 500, 500);
		cajamensajes.setVisible(true);
		cajamensajes.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane scroll=new JScrollPane(cajamensajes);
		scroll.setBounds(20,110, 500, 500);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);
	}//CAJAMENSAJES

	public static JTextArea HacerArea() {
		JTextArea cajamensajes=new JTextArea();
		cajamensajes.setBounds(20,110, 500, 500);
		cajamensajes.setVisible(true);
		cajamensajes.setBorder(BorderFactory.createLineBorder(Color.black));
		return cajamensajes;
		
	}//HACERAREA
	
	public void Contactar_Servidor() {
		try {
			clientSocket=new DatagramSocket();
			IPservidor= InetAddress.getLocalHost();
			String cadena=nickname+" se ha conectado";
			byte[] enviados= new byte[1024];		
			enviados=cadena.getBytes();		
			DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidor,puerto);
			clientSocket.send(envio);
			byte[] buffer =new byte[1024];		
			recibo = new DatagramPacket(buffer,buffer.length);
			clientSocket.receive(recibo);
			IPservidor=recibo.getAddress();
			puerto=recibo.getPort();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Enviar_Mensaje() {
		byte[] enviados= new byte[1024];		
		enviados=(nickname+" dice: "+txt.getText()).getBytes();	
		DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidor,puerto);
		try {
			clientSocket.send(envio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
	}//ENVIARMENSAJE

	public void LeerMensajes() {
		byte[] buffer =new byte[1024];		
		recibo = new DatagramPacket(buffer,buffer.length);
		try {
			clientSocket.receive(recibo);
			System.out.println("Recibi algo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//CATCH
		txts.append(new String(recibo.getData())+"\n");
	}//LEER
}//CLASS
