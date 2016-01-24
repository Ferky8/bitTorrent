package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import Entidad.Peer;
import bitTorrent.util.ByteUtils;
import java.util.Observable;
import udp.messages.AnnounceRequest;
import udp.messages.AnnounceResponse;
import udp.messages.ConnectRequest;
import udp.messages.ConnectResponse;
import udp.messages.PeerInfo;

public class GestorDeTrackerBitTorrent extends Observable implements Runnable {
	
	private static GestorDeTrackerBitTorrent gestor = null;
	private MulticastSocket udpSocket;
	private int puerto;
	private String idPeer;
	private String ipPeer;
	private int puertoPeer;
	private String infoHashPeer;
	private int percent;
	private int seeders;
	private int leechers;
	private Peer peer;
	private List<Observer> observers = new ArrayList<Observer>();
	
	private Thread hilo;
	
	public void iniciar(String IP, int puerto) {
		this.puerto = puerto;
		
		try {
			udpSocket = new MulticastSocket(puerto);
			udpSocket.joinGroup(InetAddress.getByName(IP));			
		} catch (IOException e) {
			System.err.println("# UDPServer Socket error: " + e.getMessage());
		}
		
		hilo=new Thread(this,"hilo GestorDeTrackerBitTorrent");
		hilo.start();
	}
	
	public void anadirObserver(Observer o) {
		if (o != null && !observers.contains(o)) {
			observers.add(o);
		}
	}
	
	public void eliminarObserver(Observer o) {
		observers.remove(o);
	}
	
	private void alertarObservers(Peer peer) {
		for(Observer o : observers) {
			if (o != null) {
				o.update(this, peer);
			}
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
				seeders = 0;
				leechers = 0;
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
				
				if(GestorDeRedundanciaDeTrackers.getInstance().soyMaster()) {
					ConnectResponse response = new ConnectResponse();
					response.setTransactionId(request.getTransactionId());
					response.setConnectionId(request.getConnectionId());
					
					byte[] byteMsg = response.getBytes();
					send = new DatagramPacket(byteMsg, byteMsg.length, receive.getAddress(), receive.getPort());
					udpSocket.send(send);
					
					System.out.println(" - Sent a response to '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
			                   "' -> " + new String(send.getData()) + " [" + receive.getLength() + " byte(s)]");
					System.out.println("Connect Response: " + response.getAction() + " " + response.getTransactionId() + " " + response.getConnectionId());
				}
				
				receive = new DatagramPacket(buffer, buffer.length);
				udpSocket.receive(receive);
				
				System.out.println(" - Received an announce request from '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
		                   "' -> " + new String(receive.getData()) + " [" + receive.getLength() + " byte(s)]");
				
				AnnounceRequest announceReq = AnnounceRequest.parse(receive.getData());
				
				System.out.println("Announce Request: " + announceReq.getAction() + " " + announceReq.getTransactionId() + " " + announceReq.getConnectionId()
				 + " " + announceReq.getHexInfoHash() + " " + announceReq.getPeerId()+ " " + announceReq.getLeft()+ " " + announceReq.getEvent().name());
				
				idPeer = announceReq.getPeerId();
				ipPeer = receive.getAddress().getHostAddress();
				puertoPeer = receive.getPort();
				infoHashPeer = announceReq.getHexInfoHash();
				percent = (int)(announceReq.getDownloaded() / (announceReq.getDownloaded() + announceReq.getLeft())) * 100;
				
				peer = new Peer();
				peer.setPeerId(idPeer);
				peer.setIp(ipPeer);
				peer.setPort(puertoPeer);
				peer.setInfoHash(infoHashPeer);
				peer.setDownloaded((int) announceReq.getDownloaded());
				peer.setUploaded((int) announceReq.getUploaded());
				
				List<PeerInfo> lp = null;
				if(GestorDeRedundanciaDeTrackers.getInstance().soyMaster()) {
					lp = GestorDeRedundanciaDeTrackers.getInstance().gestorDeDatos.getListaPeers(announceReq.getHexInfoHash());
				}
				
				GestorDeRedundanciaDeTrackers.getInstance().peticionPeerRecibida();	
				
				if(GestorDeRedundanciaDeTrackers.getInstance().soyMaster()) {	
					int quantity;
					List<List<PeerInfo>> llp = null;
					
					if(lp.size() > 17) {
						llp = chopped(lp, 17);
						quantity = llp.size();
						
						Iterator<List<PeerInfo>> iter = llp.iterator();
						while(iter.hasNext()) {
							Iterator<PeerInfo> iter2 = iter.next().iterator();
							while(iter2.hasNext()) {
								PeerInfo pi = iter2.next();
								int percent = GestorDeRedundanciaDeTrackers.getInstance().gestorDeDatos.getPeerTorrentPercent(ByteUtils.toStringIpAddress(pi.getIpAddress()), pi.getPort(), announceReq.getHexInfoHash());
								if(percent != 100)
									leechers++;
								else
									seeders++;
							}
						}
					} else {
						quantity = 0;
						
						Iterator<PeerInfo> iter = lp.iterator();
						while(iter.hasNext()) {
							PeerInfo pi = iter.next();
							int percent = GestorDeRedundanciaDeTrackers.getInstance().gestorDeDatos.getPeerTorrentPercent(ByteUtils.toStringIpAddress(pi.getIpAddress()), pi.getPort(), announceReq.getHexInfoHash());
							if(percent != 100)
								leechers++;
							else
								seeders++;
						}
					}
					
					int index = 0;
					
					do {	
						AnnounceResponse announceResp = new AnnounceResponse();
						announceResp.setLeechers(leechers);
						announceResp.setSeeders(seeders);
						announceResp.setTransactionId(announceReq.getTransactionId());
						if(llp != null)
							announceResp.setPeers(llp.get(index));
						else
							announceResp.setPeers(lp);
						
						if(quantity > 1)
							announceResp.setInterval(1000);
						else
							announceResp.setInterval(0);
						
						byte[] byteMsg = announceResp.getBytes();
						send = new DatagramPacket(byteMsg, byteMsg.length, receive.getAddress(), receive.getPort());
						udpSocket.send(send);
						
						System.out.println(" - Sent an announce response to '" + receive.getAddress().getHostAddress() + ":" + receive.getPort() + 
				                   "' -> " + new String(send.getData()) + " [" + receive.getLength() + " byte(s)]");
						System.out.println("Announce Response: " + announceResp.getAction() + " " + announceResp.getTransactionId());
						
						quantity--;
						index++;
						
						if(announceResp.getInterval() > 0) {
							try {
								Thread.sleep(announceResp.getInterval()+100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} while(quantity > 0);
				}	
			} catch (IOException e) {
				System.err.println("# UDPServer IO error: " + e.getMessage());
			}
		}
	}
	
	public List<List<PeerInfo>> chopped(List<PeerInfo> list, final int L) {
	    List<List<PeerInfo>> parts = new ArrayList<List<PeerInfo>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<PeerInfo>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
	
	public void guardarInformacion() {
		GestorDeRedundanciaDeTrackers.getInstance().gestorDeDatos.guardarPeer(idPeer, ipPeer, puertoPeer, infoHashPeer, percent);
		alertarObservers(peer);
	}
}
