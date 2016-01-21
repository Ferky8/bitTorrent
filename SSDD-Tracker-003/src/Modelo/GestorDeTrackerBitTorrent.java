package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class GestorDeTrackerBitTorrent implements Runnable {
	
	private static GestorDeTrackerBitTorrent gestor = null;
	private DatagramSocket udpSocket;
	private int puerto;
	
	public void iniciar(String IP, int puerto) {
		this.puerto = puerto;
		try {
			udpSocket = new DatagramSocket(puerto, InetAddress.getByName(IP));			
		} catch (SocketException | UnknownHostException e) {
			System.err.println("# UDPServer Socket error: " + e.getMessage());
		}
		
		this.run();
	}
	
	public static GestorDeTrackerBitTorrent getInstance() {
		if(gestor == null)
			gestor = new GestorDeTrackerBitTorrent();
		return gestor ;
	}
	
	public List<String> listaPeers(String torrent) {
		return null;
	}

	@Override
	public void run() {
		while(true) {
			try {
				DatagramPacket request = null;
				DatagramPacket reply = null;
				byte[] buffer = new byte[1024];
				
				System.out.println(" - Waiting for connections '" + 
				                       udpSocket.getLocalAddress().getHostAddress() + ":" + 
				                       puerto + "' ...");
				
				request = new DatagramPacket(buffer, buffer.length);
				udpSocket.receive(request);	
			} catch (IOException e) {
				System.err.println("# UDPServer IO error: " + e.getMessage());
			}
		}
	}
}
