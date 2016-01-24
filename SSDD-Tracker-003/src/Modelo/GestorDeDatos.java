package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bitTorrent.util.StringUtils;
import udp.messages.PeerInfo;

public class GestorDeDatos {

	private Connection conexion;
	private static GestorDeDatos gestor;
	
	public static GestorDeDatos getInstance(String dbname) {
		if(gestor == null)
			gestor = new GestorDeDatos(dbname);
		return gestor ;
	}
	
	private GestorDeDatos(String dbname) {
		conexion = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			conexion.setAutoCommit(false);
			
			System.out.println(" - Db connection was opened :)");
		} catch (Exception ex) {
			System.err.println(" # Unable to create SQLiteDBManager: " + ex.getMessage());
		}

		String sqlString = "SELECT * FROM Peer";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading peers from the db:");
			
			while(rs.next()) {
				System.out.println("    " + rs.getInt("ID") + ".- " + 
			                                rs.getString("IP") + ", " + 
						                    rs.getInt("Puerto"));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}	
	}
	
	public void cerrarConexion() {
		try {
			conexion.close();
			System.out.println("\n - Db connection was closed :)");
		} catch (Exception ex) {
			System.err.println("\n # Error closing db connection: " + ex.getMessage());
		}
	}
	
	public List<PeerInfo> getListaPeers(String infohash) {
		List<PeerInfo> lp = new ArrayList<PeerInfo>();
		
		String sqlString = "SELECT t1.IP, t1.Puerto FROM Peer t1 INNER JOIN Peer_Torrent t2 ON t1.Id = t2.IdPeer WHERE t2.IdTorrent = '" + infohash +"'";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading peers by InfoHash from the db:");
			
			PeerInfo pi;
			while(rs.next()) {
				pi = new PeerInfo();
				
				pi.setIpAddress(StringUtils.toIntegerIpAddress(rs.getString("IP")));
				pi.setPort(rs.getInt("Puerto"));
				
				lp.add(pi);
				
				System.out.println("    " + StringUtils.toIntegerIpAddress(rs.getString("IP")) + "---" +
											rs.getString("IP") + ", " + 
						                    rs.getInt("Puerto"));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		
		return lp;
	}
	
	public void guardarPeer(String idPeer, String IP, int puerto, String InfoHash, int percent) {
		String id = getPeerId(IP, puerto);
		String sqlString;
		
		if(!id.equals(idPeer)) {
			sqlString = "INSERT INTO 'Peer' ('Id', 'IP', 'Puerto') VALUES (?,?,?)";
		
			try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {
				stmt.setString(1, idPeer);
				stmt.setString(2, IP);
				stmt.setInt(3, puerto);
				
				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new peer was inserted. :)");
					conexion.commit();
				} else {
					System.err.println("\n - A new peer wasn't inserted. :(");
					conexion.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error storing a Peer in the db: " + ex.getMessage());
			}
			
			id = getPeerId(IP, puerto);
		}
			
		sqlString = "INSERT INTO 'Peer_Torrent' ('IdPeer', 'IdTorrent', 'Percent') VALUES (?,?,?)";
				
		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {
			stmt.setString(1, id);
			stmt.setString(2, InfoHash);
			stmt.setInt(3, percent);
			
			if (stmt.executeUpdate() == 1) {
				System.out.println("\n - A new peer_torrent was inserted. :)");
				conexion.commit();
			} else {
				System.err.println("\n - A new peer_torrent wasn't inserted. :(");
				conexion.rollback();
			}	
		} catch (Exception ex) {
			System.err.println("\n # Error storing peer_torrent in the db: " + ex.getMessage());
		}		
	}
	
	public String getPeerId(String IP, int puerto) {
		String sqlString = "SELECT id FROM Peer WHERE IP='"+ IP +"' AND Puerto="+ puerto+"";
		String id = "";
		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			id = rs.getString("ID");
			
		} catch (Exception ex) {
			System.err.println("\n # No existing Peer in the db: " + ex.getMessage());
		}
		return id;
	}
	
	public int getPeerTorrentPercent(String IP, int puerto, String torrent) {
		String sqlString = "SELECT t2.percent FROM Peer t1 INNER JOIN Peer_Torrent t2 ON t1.Id = t2.IdPeer WHERE t2.IdTorrent = '" + torrent +"' AND t1.IP = '" + IP + "' AND t1.Puerto = " + puerto + "";
		int percent = 0;
		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			percent = rs.getInt("percent");
			
		} catch (Exception ex) {
			System.err.println("\n # Error retrieving percent: " + ex.getMessage());
		}
		return percent;
	}
	
	public void insertarDatos(List<String> listaInsertar) {
		
	}
	
	public void actualizarDatos(List<String> listaActuaizar) {
		
	}
	
	public List<String> cargarDatos() {
		return null;
	}
	
	public void eliminarDatos(List<String> listaEliminar) {
		
	}
	
	public static void main(String[] args) {
		
	}

	public Connection getConexion() {
		return conexion;
	}

	public void setConexion(Connection conexion) {
		
	}
	
	public void hayGuardar() {
		
	}
	
	public void preparadoGuardar() {
		
	}
	
	public void OKGuardar() {
		
	}
}
