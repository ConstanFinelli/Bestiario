package data;

import entities.TipoEvidencia;
import exceptions.DataNotFoundException;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTipoEvidencia {

	private static final Logger logger = Logger.getLogger(DataTipoEvidencia.class.getName());

	public TipoEvidencia getOne(TipoEvidencia tipoE) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TipoEvidencia tipoEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from tipo_evidencia where idTipoEvidencia = ?");
			pstmt.setInt(1, tipoE.getId());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idTipoEvidencia");
				String descripcion = rs.getString("descripcion");
				String resourceType = rs.getString("resourceType");
				tipoEncontrado = new TipoEvidencia(id, descripcion, resourceType);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getOne de tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(tipoEncontrado == null) {
			throw new DataNotFoundException("No se encontró el tipo de evidencia con id " + tipoE.getId());
		}
		return tipoEncontrado;
	}
	
	public LinkedList<TipoEvidencia> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<TipoEvidencia> tipos = new LinkedList<>();
		TipoEvidencia tipoEncontrado = null;
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from tipo_evidencia");
			if(rs != null){
				while(rs.next()) {
					int id = rs.getInt("idTipoEvidencia");
					String descripcion = rs.getString("descripcion");
					String resourceType = rs.getString("resourceType");
					tipoEncontrado = new TipoEvidencia(id, descripcion, resourceType);
					tipos.add(tipoEncontrado);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar tipos de evidencia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAll de tipos de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return tipos;
	} 
	
	public TipoEvidencia save(TipoEvidencia tipoS) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into tipo_evidencia(descripcion, resourceType) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, tipoS.getDescripcion());
			pstmt.setString(2, tipoS.getResourceType());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int id = rs.getInt(1);
				tipoS.setId(id);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en save de tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return tipoS;
	}
	
	public TipoEvidencia update(TipoEvidencia datos) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update tipo_evidencia set descripcion = ?, resourceType = ? where idTipoEvidencia = ?");
			pstmt.setString(1, datos.getDescripcion());
			pstmt.setInt(2, datos.getId());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				datos = null;
			}
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(datos == null) {
			throw new DataNotFoundException("No se encontró el tipo de evidencia a actualizar.");
		}
		return datos;
	}
	
	public TipoEvidencia delete(TipoEvidencia datoBorrado) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from tipo_evidencia where idTipoEvidencia = ?");
			pstmt.setInt(1, datoBorrado.getId());
			pstmt.executeUpdate();
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de tipo de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return datoBorrado;
	}
}
