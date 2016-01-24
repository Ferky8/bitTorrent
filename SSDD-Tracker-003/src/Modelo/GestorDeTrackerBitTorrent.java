package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import udp.messages.AnnounceRequest;
import udp.messages.AnnounceResponse;
import udp.messages.ConnectRequest;
import udp.messages.ConnectResponse;

public class GestorDeTrackerBitTorrent implements Runnable {
	
	private static GestorDeTrackerBitTorrent gestor = null;
	private DatagramSocket udpSocket;
	private int puerto;
	
	private Thread hilo;
	
	public void iniciar(String IP, int puerto) {
		this.puerto = puerto;

		hilo=new Thread(this,"hilo GestorDeTrackerBitTorrent");
		hilo.start();
		
		try {
			udpSocket = new DatagramSocket(puerto, InetAddress.getByName(IP));			
		} catch (SocketException | UnknownHostException e) {
			System.err.println("# UDPServer Socket error: " + e.getMessage());
		}
		
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
				DatagramPacket receive = null;
				DatagramPacket send = null;
				byte[] buffer = new byte[1024];
				
				System.out.println(" - Waiting for connections '" + 
				                       udpSocket.getLocalAddress().getHostAddress() + ":" + 
				                       puerto + "' ...");
				
				receive = new DatagramPacket(buffer, buffer.length);
				udpSocket.receive(receive);	
				
				System.out.println(" - Received a request from '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
		                   "' -> " + new String(receive.getData()) + " [" + receive.getLength() + " byte(s)]");
								
				ConnectRequest request = ConnectRequest.parse(receive.getData());
				 
				System.out.println("Connect Request: " + request.getAction() + " " + request.getTransactionId() + " " + request.getConnectionId());
				
				ConnectResponse response = new ConnectResponse();
				response.setTransactionId(request.getTransactionId());
				response.setConnectionId(request.getConnectionId());
				
				byte[] byteMsg = response.getBytes();
				send = new DatagramPacket(byteMsg, byteMsg.length, receive.getAddress(), receive.getPort());
				udpSocket.send(send);
				
				System.out.println(" - Sent a response to '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
		                   "' -> " + new String(send.getData()) + " [" + receive.getLength() + " byte(s)]");
				System.out.println("Connect Response: " + response.getAction() + " " + response.getTransactionId() + " " + response.getConnectionId());
				
				receive = new DatagramPacket(buffer, buffer.length);
				udpSocket.receive(receive);
				
				System.out.println(" - Received an announce request from '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
		                   "' -> " + new String(receive.getData()) + " [" + receive.getLength() + " byte(s)]");
				
				AnnounceRequest announceReq = AnnounceRequest.parse(receive.getData());
				
				System.out.println("Announce Request: " + announceReq.getAction() + " " + announceReq.getTransactionId() + " " + announceReq.getConnectionId()
				 + " " + announceReq.getHexInfoHash() + " " + announceReq.getPeerId()+ " " + announceReq.getLeft()+ " " + announceReq.getEvent().name());
				
				AnnounceResponse announceResp = new AnnounceResponse();
				announceResp.setInterval(3);
				announceResp.setLeechers(5);
				announceResp.setSeeders(4);
				announceResp.setTransactionId(announceReq.getTransactionId());
				announceResp.setPeers(GestorDeRedundanciaDeTrackers.getInstance().gestorDeDatos.getListaPeers(announceReq.getHexInfoHash()));
				
				//Guardar la informacion del Peer en la DB
				
				byteMsg = announceResp.getBytes();
				send = new DatagramPacket(byteMsg, byteMsg.length, receive.getAddress(), receive.getPort());
				udpSocket.send(send);
				
				System.out.println(" - Sent an announce response to '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
		                   "' -> " + new String(send.getData()) + " [" + receive.getLength() + " byte(s)]");
				System.out.println("Announce Response: " + announceResp.getAction() + " " + announceResp.getTransactionId());
				
			} catch (IOException e) {
				System.err.println("# UDPServer IO error: " + e.getMessage());
			}
		}
	}
}
