package data;

import entities.Habitat;

import java.sql.*;
import java.util.LinkedList;

public class DataHabitat {
	public Habitat getOne(Habitat ht) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtC = null;
		ResultSet rsC = null;
		Habitat htEncontrada = null;
		LinkedList<String> caracteristicas = new LinkedList<>();
		
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM habitat WHERE idHabitat=?");
			pstmt.setInt(1, ht.getId());
			rs = pstmt.executeQuery();
			pstmtC = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM caracteristica WHERE idHabitat=?");
			pstmtC.setInt(1, ht.getId());
			rsC = pstmtC.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idHabitat");
				String localizacion = rs.getString("localizacion");
				String nombre = rs.getString("nombre");
				if(rsC != null) {
					while(rsC.next()){
						caracteristicas.add(rsC.getString("descripcion"));
					}
				}
				htEncontrada = new Habitat(id, nombre, caracteristicas, localizacion);
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
			try {
				if(rs != null) {rs.close();}
				if(pstmt != null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return htEncontrada;
	}
	
	public LinkedList<Habitat> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtC = null;
		ResultSet rsC = null;
		LinkedList<Habitat> htsEncontradas = new LinkedList<>();
		Habitat htEncontrada = null;
		
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("SELECT * FROM habitat");
			if(rs != null) {
				while(rs.next()) {
					LinkedList<String> caracteristicas = new LinkedList<>();
					int id = rs.getInt("idHabitat");
					String localizacion = rs.getString("localizacion");
					String nombre = rs.getString("nombre");
					try {
						pstmtC = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM caracteristica WHERE idHabitat=?");
						pstmtC.setInt(1, id);
						rsC = pstmtC.executeQuery();
						if(rsC != null) {
							while(rsC.next()) {
								caracteristicas.add(rsC.getString("descripcion"));
							}
						}
					}
					catch(SQLException ex){
						System.out.println("Mensaje: " + ex.getMessage());
			            System.out.println("SQLState: " + ex.getSQLState());
			            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
					}
					htEncontrada = new Habitat(id, nombre, caracteristicas, localizacion);
					htsEncontradas.add(htEncontrada);
				}
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
			try {
				if(rs != null) {rs.close();}
				if(stmt != null) {stmt.close();}
				if(rsC != null) {rsC.close();}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return htsEncontradas;
	}
	
	public Habitat save(Habitat ht) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Habitat htGuardada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("INSERT INTO habitat(nombre, localizacion) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, ht.getNombre());
			pstmt.setString(2, ht.getLocalizacion());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				htGuardada = ht;
				htGuardada.setCaracteristicas(null);
				htGuardada.setId(rs.getInt(1));
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
			try {
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return htGuardada;
	}
	
	public Habitat update(Habitat ht) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("UPDATE habitat SET nombre = ?, localizacion = ? WHERE idHabitat = ?");
			pstmt.setString(1, ht.getNombre());
			pstmt.setString(2, ht.getLocalizacion());
			pstmt.setInt(3, ht.getId());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				ht = null;
			}
		}catch(SQLException ex){
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return ht;
	}
	
	public Habitat delete(Habitat ht) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("DELETE FROM habitat WHERE idHabitat = ?");
			pstmt.setInt(1, ht.getId());
			pstmt.executeUpdate();
		}catch(SQLException ex){
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return ht;
	}
}
