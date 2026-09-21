package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Evidencia;
import entities.TipoEvidencia;
import exceptions.DataNotFoundException;

public class DataEvidencia {
	private static final Logger logger = Logger.getLogger(DataEvidencia.class.getName());
	public DataTipoEvidencia teDao = new DataTipoEvidencia();
	
	public Evidencia getOne(Evidencia e) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Evidencia evidenciaEncontrada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from evidencia where nroEvidencia = ? and idTipoEvidencia = ?");
			pstmt.setInt(1, e.getNroEvidencia());
			pstmt.setInt(2, e.getTipo().getId());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("nroEvidencia");
				LocalDate fechaO = rs.getDate("fechaObtencion").toLocalDate();
				String estado = rs.getString("estado");
				String fileId = rs.getString("fileId");
				int idTipo = rs.getInt("idTipoEvidencia");
				TipoEvidencia tipo = teDao.getOne(new TipoEvidencia(idTipo));
				evidenciaEncontrada = new Evidencia(id, fechaO, estado, fileId, tipo);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener evidencia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getOne de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(evidenciaEncontrada == null) {
			throw new DataNotFoundException("No se encontró la evidencia solicitada.");
		}
		return evidenciaEncontrada;
	}
	
	public LinkedList<Evidencia> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from evidencia");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroEvidencia");
					LocalDate fechaO = rs.getDate("fechaObtencion").toLocalDate();
					String estado = rs.getString("estado");
					String fileId = rs.getString("fileId");
					int idTipo = rs.getInt("idTipoEvidencia");
					TipoEvidencia tipo = teDao.getOne(new TipoEvidencia(idTipo));
					Evidencia evidencia = new Evidencia(id, fechaO, estado, fileId, tipo);
					evidencias.add(evidencia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar evidencias [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAll de evidencias [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return evidencias;
	}
	
	public Evidencia save(Evidencia e) {
		asignarNroEvidencia(e);
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into evidencia(nroEvidencia, fechaObtencion, estado, fileId, idTipoEvidencia) values(?, ?, ?, ? ,?)");
			pstmt.setInt(1, e.getNroEvidencia()); 
			pstmt.setDate(2, java.sql.Date.valueOf(e.getFechaObtencion()));
			pstmt.setString(3, e.getEstado());
			pstmt.setString(4, e.getFileId());
			pstmt.setInt(5, e.getTipo().getId());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en save de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return e;
	}
	
	public Evidencia update(Evidencia e) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update evidencia set fechaObtencion = ?, estado = ?, fileId = ? where idTipoEvidencia = ? and nroEvidencia = ?");
			pstmt.setDate(1, java.sql.Date.valueOf(e.getFechaObtencion()));
			pstmt.setString(2, e.getEstado());
			pstmt.setString(3, e.getFileId());
			pstmt.setInt(4,e.getTipo().getId());
			pstmt.setInt(5, e.getNroEvidencia());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				e = null;
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(e == null) {
			throw new DataNotFoundException("No se encontró la evidencia a actualizar.");
		}
		return e;
	}
	
	public Evidencia delete(Evidencia e) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from evidencia where idTipoEvidencia = ? and nroEvidencia = ?");
			pstmt.setInt(1, e.getTipo().getId());
			pstmt.setInt(2, e.getNroEvidencia());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return e;
	}
	
	public void asignarNroEvidencia(Evidencia e) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select max(nroEvidencia) as maxnumber from evidencia where idTipoEvidencia = ?");
			pstmt.setInt(1, e.getTipo().getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				e.setNroEvidencia(rs.getInt("maxnumber") + 1);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al asignar nro de evidencia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en asignarNroEvidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public LinkedList<Evidencia> findAllType(TipoEvidencia te){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from evidencia where idTipoEvidencia = ?");
			pstmt.setInt(1, te.getId());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroEvidencia");
					LocalDate fechaO = rs.getDate("fechaObtencion").toLocalDate();
					String estado = rs.getString("estado");
					String fileId = rs.getString("fileId");
					Evidencia evidencia = new Evidencia(id, fechaO, estado, fileId, te);
					evidencias.add(evidencia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar evidencias por tipo [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAllType de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return evidencias;
	}
	
	public LinkedList<Evidencia> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from evidencia ev inner join bestia_evidencia bv on ev.nroEvidencia = bv.nroEvidencia inner join bestia b on bv.idBestia = b.idBestia where b.idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int nroEvidencia = rs.getInt("nroEvidencia");
					LocalDate fechaO = rs.getDate("fechaObtencion").toLocalDate();
					String estado = rs.getString("estado");
					String fileId = rs.getString("fileId");
					int idTipo = rs.getInt("idTipoEvidencia");
					TipoEvidencia te = teDao.getOne(new TipoEvidencia(idTipo));
					Evidencia evidencia = new Evidencia(nroEvidencia,fechaO, estado, fileId, te);
					evidencias.add(evidencia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar evidencias por bestia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAllByBestia de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return evidencias;
	}
	
	public boolean updateEstado(int nroEvidencia, int idTipoEvidencia, String nuevoEstado) throws DataNotFoundException {
		PreparedStatement pstmt = null;
		boolean actualizado = false;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update evidencia set estado = ? where idTipoEvidencia = ? and nroEvidencia = ?");
			pstmt.setString(1, nuevoEstado);
			pstmt.setInt(2, idTipoEvidencia);
			pstmt.setInt(3, nroEvidencia);
			int rows = pstmt.executeUpdate();
			actualizado = (rows > 0);
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar estado de evidencia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en updateEstado de evidencia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(!actualizado) {
			throw new DataNotFoundException("No se encontró la evidencia para actualizar su estado.");
		}
		return actualizado;
	}
}
