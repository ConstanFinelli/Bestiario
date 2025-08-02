package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import entities.*;

public class DataBestia {
	public Bestia getOne(Bestia b) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Bestia bestiaEncontrada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from bestia where idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idBestia");
				String nombre = rs.getString("nombre");
				String peligrosidad = rs.getString("peligrosidad");
				bestiaEncontrada = new Bestia(id, nombre, peligrosidad);
				completarBestia(bestiaEncontrada);
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
		return bestiaEncontrada;
	}
	
	public LinkedList<Bestia> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		Bestia bestia = null;
		LinkedList<Bestia> bestias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from bestia");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idBestia");
					String nombre = rs.getString("nombre");
					String peligrosidad = rs.getString("peligrosidad");
					bestia = new Bestia(id, nombre, peligrosidad);
					completarBestia(bestia);
					bestias.add(bestia);
				}
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return bestias;
	}
	
	public Bestia save(Bestia bestia) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia(nombre, peligrosidad) values(?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, bestia.getNombre());
			pstmt.setString(2, bestia.getPeligrosidad());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				bestia.setIdBestia(rs.getInt(1));
			}
		}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			} finally {
				try {
					if(rs != null) {
						rs.close();
					}
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
		return bestia;
		}
	
	public Bestia update(Bestia bestia) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update bestia set nombre = ?, peligrosidad = ? where idBestia = ?");
			pstmt.setString(1, bestia.getNombre());
			pstmt.setString(2, bestia.getPeligrosidad());
			pstmt.setInt(3, bestia.getIdBestia());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				bestia = null;
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
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
		return bestia;
	}
	
	public Bestia delete(Bestia bestiaBorrada) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia where idBestia = ?");
			pstmt.setInt(1, bestiaBorrada.getIdBestia());
			pstmt.executeUpdate();
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
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
		return bestiaBorrada;
	}
	
	public void addCategorias(Bestia bestiaEncontrada){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from categoria c join bestia_categoria bc on bc.idCategoria = c.idCategoria where bc.idBestia = ?");
			pstmt.setInt(1, bestiaEncontrada.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idCategoria");
					String nombre = rs.getString("nombre");
					String desc = rs.getString("descripcion");
					Categoria categoria = new Categoria(id, nombre, desc);
					bestiaEncontrada.getCategorias().add(categoria);
				}
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
		}
	public void addHabitats(Bestia bestiaEncontrada) {
		
	}
	public void addRegistros(Bestia bestiaEncontrada) {
		
	}
	
	public void completarBestia(Bestia bestia) {
		//addRegistros(bestia);
		//addHabitats(bestia);
		//addCategorias(bestia);
	}
}

