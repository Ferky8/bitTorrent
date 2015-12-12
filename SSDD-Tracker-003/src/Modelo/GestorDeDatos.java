package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class GestorDeDatos {

	private Connection conexion;

	public GestorDeDatos(String dbname) {
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
	    
//		sqlString = "INSERT INTO 'Peer' ('IP', 'Puerto') VALUES (?,?)";
//		
//		try (PreparedStatement stmt = conexion.prepareStatement(sqlString)) {
//			stmt.setString(1, "12.45.67.89");
//			stmt.setInt(2, 1234);
//			
//			if (stmt.executeUpdate() == 1) {
//				System.out.println("\n - A new peer was inserted. :)");
//				conexion.commit();
//			} else {
//				System.err.println("\n - A new peer wasn't inserted. :(");
//				conexion.rollback();
//			}	
//		} catch (Exception ex) {
//			System.err.println("\n # Error storing data in the db: " + ex.getMessage());
//		}
	}
	
	public void cerrarConexion() {
		try {
			conexion.close();
			System.out.println("\n - Db connection was closed :)");
		} catch (Exception ex) {
			System.err.println("\n # Error closing db connection: " + ex.getMessage());
		}
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
