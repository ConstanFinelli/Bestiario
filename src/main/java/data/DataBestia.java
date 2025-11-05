package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import entities.*;

//INCOMPLETO: DEBATIR EN GRUPO COMO PROSEGUIR Y ORGANIZAR EL TEMA DE CATEGORIAS, HABITATS Y REGISTROS EN EL SAVE
public class DataBestia {
	public DataHabitat habDAO = new DataHabitat();
	public DataCategoria catDAO = new DataCategoria();
	public DataRegistro regDAO = new DataRegistro();
	public DataComentario comDAO = new DataComentario();
	public DataEvidencia evDAO = new DataEvidencia();
	
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
	
	public LinkedList<Bestia> findByCategoria (String categoria){
		Statement stmt = null;
		ResultSet rs = null;
		Bestia bestia = null;
		
		LinkedList<Bestia> bestias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select b.* from bestia b "
					+ "inner join bestia_categoria bc on bc.idBestia = b.idBestia"
					+ " inner join categoria c on c.idCategoria = bc.idCategoria "
					+ "where c.nombre = '" + categoria + "'");
			
			System.out.println(rs.toString());
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
	
	public Bestia save(Bestia b) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into Bestia(nombre, peligrosidad) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, b.getNombre());
			pstmt.setString(2, b.getPeligrosidad());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				b.setIdBestia(rs.getInt(1));
			}
			saveCategorias(b);
			saveRegistros(b);
			saveHabitats(b);
			saveEvidencias(b);
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
		return b;
		}
	
	public Bestia update(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update Bestia set nombre = ?, peligrosidad = ? where idBestia = ?");
			pstmt.setString(1, b.getNombre());
			pstmt.setString(2, b.getPeligrosidad());
			pstmt.setInt(3, b.getIdBestia());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				b = null;
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
		return b;
	}
	
	public Bestia delete(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.executeUpdate();
			deleteCategorias(b);
			deleteHabitats(b);
			//preguntar si se deberian eliminar las bestias
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
		return b;
	}
	
	public void addCategorias(Bestia bestiaEncontrada){
		bestiaEncontrada.setCategorias(catDAO.findAllByBestia(bestiaEncontrada));
		}
	public void addHabitats(Bestia bestiaEncontrada) {
		bestiaEncontrada.setHabitats(habDAO.findAllByBestia(bestiaEncontrada));
	}
	public void addRegistros(Bestia bestiaEncontrada) {
		bestiaEncontrada.setRegistros(regDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void addComentarios(Bestia bestiaEncontrada) {
		bestiaEncontrada.setComentarios(comDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void addEvidencias(Bestia bestiaEncontrada) {
		bestiaEncontrada.setEvidencias(evDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void completarBestia(Bestia bestia) {
		addRegistros(bestia);
		addHabitats(bestia);
		addCategorias(bestia);
		addComentarios(bestia);
		addEvidencias(bestia);
	}
	
	public void saveCategorias(Bestia b) {
		PreparedStatement pstmt = null;
		for(Categoria categoria : b.getCategorias()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_categoria(idBestia, idCategoria) values(?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, categoria.getIdCategoria());
				pstmt.executeQuery();
			}catch(SQLException ex){
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
		}	
	}
	
	public void saveHabitats(Bestia b) {
		PreparedStatement pstmt = null;
		for(Habitat habitat : b.getHabitats()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_habitat(idBestia, idHabitat) values(?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, habitat.getId());
				pstmt.executeQuery();
			}catch(SQLException ex){
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
		}	
	}
	
	public void saveEvidencias(Bestia b) {
		PreparedStatement pstmt = null;
		for(Evidencia evidencia : b.getEvidencias()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_evidencia(idBestia, nroEvidencia, idTipoEvidencia ,detalle) values(?,?,?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, evidencia.getNroEvidencia());
				pstmt.setInt(3, evidencia.getTipo().getId());
				pstmt.setString(4, "Detalle a actualizar");
				pstmt.executeUpdate();
			}catch(SQLException ex){
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
		}	
	}
	
	public void saveRegistros(Bestia b) {
		for(Registro registro : b.getRegistros()) {
			regDAO.save(registro);
		}
	}
	
	public void deleteCategorias(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_categoria where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
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
	}
	
	public void deleteHabitats(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_habitat where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
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
	}
	
	public LinkedList<Bestia> findAllBestiasFromHabitat(Habitat ht){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Bestia> bestiasHabitat = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct * from habitat h join bestia_habitat bh on bh.idHabitat = h.idHabitat where bh.idHabitat = ?");
			pstmt.setInt(1, ht.getId());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Bestia bestia = new Bestia(rs.getInt("idBestia"), null, null);
					bestia = this.getOne(bestia);
					bestiasHabitat.add(bestia);
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
		return bestiasHabitat;
	}
}

