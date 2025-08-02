package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;

import entities.*;
import logic.*;

public class DataBestia {
	public LogicHabitat controladorHabitat = new LogicHabitat();
	public LogicCategoria controladorCategoria = new LogicCategoria();
	public LogicRegistro controladorRegistro = new LogicRegistro();
	
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
	
	public Categoria save(Categoria cat) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into Bestia(descripcion) values(?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cat.getDescripcionCategoria());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				cat.setIdCategoria(rs.getInt(1));
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
		return cat;
		}
	
	public Categoria update(Categoria cat) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update categoria set descripcion = ? where idCategoria = ?");
			pstmt.setString(1, cat.getDescripcionCategoria());
			pstmt.setInt(2, cat.getIdCategoria());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				cat = null;
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
		return cat;
	}
	
	public Categoria delete(Categoria catBorrada) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from categoria where idCategoria = ?");
			pstmt.setInt(1, catBorrada.getIdCategoria());
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
		return catBorrada;
	}
	
	public void addCategorias(Bestia bestiaEncontrada){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct idCategoria from categoria c join bestia_categoria bc on bc.idCategoria = c.idCategoria where bc.idBestia = ?");
			pstmt.setInt(1, bestiaEncontrada.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Categoria categoria = new Categoria(rs.getInt("idCategoria"),null, null);
					categoria = controladorCategoria.getOne(categoria);
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct idHabitat from habitat h join bestia_habitat bh on bh.idHabitat = h.idHabitat where bh.idBestia = ?");
			pstmt.setInt(1, bestiaEncontrada.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Habitat habitat = new Habitat(rs.getInt("idHabitat"), null, null, null);
					habitat = controladorHabitat.getOne(habitat);
					bestiaEncontrada.getHabitats().add(habitat);
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
	public void addRegistros(Bestia bestiaEncontrada) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct nroRegistro from Registros where idBestia = ?");
			pstmt.setInt(1, bestiaEncontrada.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Registro registro = new Registro(rs.getInt("nroRegistro"), bestiaEncontrada);
					registro = controladorRegistro.getOne(registro);
					bestiaEncontrada.getRegistros().add(registro);
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
	
	public void completarBestia(Bestia bestia) {
		addRegistros(bestia);
		addHabitats(bestia);
		addCategorias(bestia);
	}
}

