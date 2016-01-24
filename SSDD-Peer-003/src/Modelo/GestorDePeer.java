package Modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import udp.messages.AnnounceRequest;
import udp.messages.AnnounceResponse;
import udp.messages.BitTorrentUDPMessage.Action;
import udp.messages.ConnectResponse;
import udp.messages.PeerInfo;
import udp.messages.AnnounceRequest.Event;
import bitTorrent.util.ByteUtils;
import udp.messages.ConnectRequest;

public class GestorDePeer implements Runnable {

	public static GestorDePeer gestor = null;
	private String IP;
	private int puerto;
	private long ConnectionID;
	
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
			
				InetAddress serverHost = InetAddress.getByName(IP);	
				
				Random random = new Random();
				int transactionID = random.nextInt(Integer.MAX_VALUE);
				 
				ConnectRequest request = new ConnectRequest();
				request.setTransactionId(transactionID);
								 
				byte[] byteMsg = request.getBytes();
				DatagramPacket packet = new DatagramPacket(byteMsg, byteMsg.length, serverHost, puerto);
				udpSocket.send(packet);
				System.out.println(" - Sent a request to '" + IP + ":" + packet.getPort() + 
						           "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
				System.out.println("Request: " + request.getAction() + " " + request.getTransactionId() + " " + request.getConnectionId());
				
				byte[] result = new byte[128];

				packet = new DatagramPacket(result, result.length);
				udpSocket.receive(packet);
				
				if (packet.getLength() >= 16) {
					 ConnectResponse response = ConnectResponse.parse(packet.getData());
					 
					 System.out.println(" - Received a response from '" + IP + ":" + packet.getPort() + 
					           "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
					 System.out.println("Connect Response: " + response.getAction() + " " + response.getTransactionId() + " " + response.getConnectionId());
					 
					 if(response.getAction() == Action.CONNECT && request.getTransactionId() == response.getTransactionId()) {
						 ConnectionID = response.getConnectionId();
						 
						 int transactionID2 = random.nextInt(Integer.MAX_VALUE);
							
						 AnnounceRequest announce = new AnnounceRequest();
						 
						 announce.setConnectionId(ConnectionID);
						 announce.setTransactionId(transactionID2);
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
						 
						 byteMsg = announce.getBytes();
						 packet = new DatagramPacket(byteMsg, byteMsg.length, serverHost, puerto);
						 udpSocket.send(packet);
						 
						 System.out.println(" - Sent an announce request to '" + IP + ":" + packet.getPort() + 
						           "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
						 System.out.println("Announce Request: " + announce.getAction() + " " + announce.getTransactionId() + " " + announce.getConnectionId()
						 + " " + announce.getHexInfoHash() + " " + announce.getPeerId()+ " " + announce.getLeft()+ " " + announce.getEvent().name());
						 
						 int interval;
						 do {
							 result = new byte[128];				 
							 packet = new DatagramPacket(result, result.length);
							 udpSocket.receive(packet);
							 
							 System.out.println(" - Received an announce response from '" + IP + ":" + packet.getPort() + 
							           "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
							 System.out.println("Announce Response: " + packet.getData().length);
							 
							 AnnounceResponse aResponse = AnnounceResponse.parse(packet.getData());
							 
							 interval = aResponse.getInterval();
							 System.out.println("Seeders: " + aResponse.getSeeders() + 
									            " - Leechers: " + aResponse.getLeechers() + 
									            " - Interval: " + aResponse.getInterval() );
							 
							 for (PeerInfo pInfo : aResponse.getPeers()) {
								 System.out.println(pInfo);
							 }
							 
							 if(interval != 0) {
								 Thread.sleep(interval);
							 }
						 } while(interval != 0);
					 }
				 } else {
					 System.err.println("- Response length is: " + packet.getLength());
				 }				
			} catch (Exception ex) {
				System.err.println("Error: " + ex.getMessage());
			}
	}
	
}
