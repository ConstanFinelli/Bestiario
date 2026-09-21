package data;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Categoria;

public class DataCategoria {

	private static final Logger logger = Logger.getLogger(DataCategoria.class.getName());

	public Categoria getOne(Categoria catB) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Categoria categoriaEncontrada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from categoria where idCategoria = ?");
			pstmt.setInt(1, catB.getIdCategoria());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idCategoria");
				String nombre = rs.getString("nombre");
				String desc = rs.getString("descripcion");
				categoriaEncontrada = new Categoria(id, nombre, desc);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener categoria [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en getOne de categoria [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return categoriaEncontrada;
	}
	
	public LinkedList<Categoria> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<Categoria> categorias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from categoria");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idCategoria");
					String nombre = rs.getString("nombre");
					String desc = rs.getString("descripcion");
					Categoria categoria = new Categoria(id, nombre, desc);
					categorias.add(categoria);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar categorias [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAll de categorias [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return categorias;
	}
	
	public Categoria save(Categoria cat) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into categoria(nombre, descripcion) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cat.getNombre());
			pstmt.setString(2, cat.getDescripcionCategoria());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				cat.setIdCategoria(rs.getInt(1));
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar categoria [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en save de categoria [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return cat;
	}
	
	public Categoria update(Categoria cat) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update categoria set nombre = ?, descripcion = ? where idCategoria = ?");
			pstmt.setString(1, cat.getNombre());
			pstmt.setString(2, cat.getDescripcionCategoria());
			pstmt.setInt(3, cat.getIdCategoria());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				cat = null;
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar categoria [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de categoria [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
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
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar categoria [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de categoria [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return catBorrada;
	}
	
	public LinkedList<Categoria> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Categoria> categorias = new LinkedList<Categoria>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct bc.idCategoria from categoria c join bestia_categoria bc on bc.idCategoria = c.idCategoria where bc.idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Categoria categoria = new Categoria(rs.getInt("idCategoria"),null, null);
					categoria = this.getOne(categoria);
					categorias.add(categoria);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar categorias por bestia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAllByBestia de categoria [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return categorias;
	}
}
