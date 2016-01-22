package Modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import udp.messages.AnnounceRequest;
import udp.messages.AnnounceResponse;
import udp.messages.ConnectResponse;
import udp.messages.PeerInfo;
import udp.messages.AnnounceRequest.Event;
import bitTorrent.util.ByteUtils;
import udp.messages.ConnectRequest;

public class GestorDePeer implements Runnable {

	public static GestorDePeer gestor = null;
	private String IP;
	private int puerto;
	
	private Thread hilo;
	
	public static GestorDePeer getInstance() {
		if(gestor == null)
			gestor = new GestorDePeer();
		return gestor ;
	}
	
	public void iniciar(String IP, int puerto) {
		this.IP = IP;
		this.puerto = puerto;
		
		hilo=new Thread(this,"hilo Peer");
		hilo.start();
	}

	@Override
	public void run() {
		
			try (DatagramSocket udpSocket = new DatagramSocket()) {
			
				String message = "Hola";
				
				InetAddress serverHost = InetAddress.getByName(IP);	
				
				Random random = new Random();
				int transactionID = random.nextInt(Integer.MAX_VALUE);
				 
				ConnectRequest request = new ConnectRequest();
				request.setTransactionId(transactionID);
				System.out.println("Request: " + request.getAction() + " " + request.getTransactionId() + " " + request.getConnectionId());
				 
				byte[] byteMsg = request.getBytes();
				DatagramPacket packet = new DatagramPacket(byteMsg, byteMsg.length, serverHost, 6969);
				udpSocket.send(packet);
				System.out.println(" - Sent request to '" + IP + ":" + packet.getPort() + 
						           "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
				
				byte[] result = new byte[128];

				packet = new DatagramPacket(result, result.length);
				udpSocket.receive(packet);
				
				if (packet.getLength() >= 16) {
					 ConnectResponse response = ConnectResponse.parse(packet.getData());
					 
					 System.out.println("Connect Response: " + response.getAction() + " " + response.getTransactionId() + " " + response.getConnectionId());
					 
					 AnnounceRequest announce = new AnnounceRequest();
					 
					 announce.setConnectionId(response.getConnectionId());
					 announce.setTransactionId(response.getTransactionId());
					 //UPDATE: 23/12/2015
					 announce.setInfoHash(ByteUtils.toByteArray("916A6189FFB20F0B20739E6F760C99174625DC2B"));				 
					 announce.setPeerId(ByteUtils.createPeerId());				 
					 announce.setDownloaded(0);
					 announce.setUploaded(0);
					 announce.setLeft(229984459);
					 announce.setEvent(Event.STARTED);
					 announce.getPeerInfo().setIpAddress(0);
					 announce.setKey(new Random().nextInt(Integer.MAX_VALUE));
					 announce.setNumWant(-1);
					 announce.getPeerInfo().setPort(28159);
					 
					 byteMsg = request.getBytes();
					 packet = new DatagramPacket(byteMsg, byteMsg.length, serverHost, 1337);
					 udpSocket.send(packet);
					 
					 result = new byte[128];				 
					 packet = new DatagramPacket(result, result.length);
					 udpSocket.receive(packet);
					 
					 System.out.println("Announce Response: " + packet.getData().length);
					 
					 AnnounceResponse aResponse = AnnounceResponse.parse(packet.getData());
					 
					 System.out.println("Seeders: " + aResponse.getSeeders() + 
							            " - Leechers: " + aResponse.getLeechers() + 
							            " - Interval: " + aResponse.getInterval() );
					 
					 for (PeerInfo pInfo : aResponse.getPeers()) {
						 System.out.println(pInfo);
					 }
					 
				 } else {
					 System.err.println("- Response length is: " + packet.getLength());
				 }				
			} catch (Exception ex) {
				System.err.println("Error: " + ex.getMessage());
			}
	}
	
}