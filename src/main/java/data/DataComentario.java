package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.*;
import exceptions.DataNotFoundException;

public class DataComentario {
	private static final Logger logger = Logger.getLogger(DataComentario.class.getName());
	public DataUsuario userDAO = new DataUsuario();
	
	public Comentario getOne(Comentario c) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comentario comentarioEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from comentario where idUsuario = ?, idBestia = ?, fechaPublicacion = ?");
			pstmt.setInt(1, c.getPublicador().getIdUsuario());
			pstmt.setInt(2, c.getBestia().getIdBestia());
			pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(c.getFecha()));
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				comentarioEncontrado = new Comentario(c.getPublicador(), c.getBestia(), c.getFecha(), rs.getString("contenido"));
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener comentario [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getOne de comentario [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(comentarioEncontrado == null) {
			throw new DataNotFoundException("No se encontró el comentario solicitado.");
		}
		return comentarioEncontrado;
	} 
	
	public LinkedList<Comentario> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comentario comentario = null;
		LinkedList<Comentario> comentarios = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from comentario where idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int idUsuario = rs.getInt("IdUsuario");
					String contenido = rs.getString("contenido");
					LocalDateTime fecha = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
					Bestia bestia = b;
					Usuario user = userDAO.getOne(new Usuario(idUsuario));
					comentario = new Comentario(user, bestia, fecha, contenido);
					comentarios.add(comentario);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar comentarios por bestia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAllByBestia de comentario [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return comentarios;
	}

	public Comentario save(Comentario c) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into comentario(idUsuario, idBestia, contenido, fechapublicacion) values(?, ?, ?, ?)");
			pstmt.setInt(1, c.getPublicador().getIdUsuario());
			pstmt.setInt(2, c.getBestia().getIdBestia());
			pstmt.setString(3, c.getContenido());
			pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(c.getFecha()));
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar comentario [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en save de comentario [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return c;
	}
	
	public Comentario update(Comentario c, LocalDateTime oldDate) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update comentario set contenido = ?, fechaPublicacion = ? where idBestia = ? and idUsuario = ? and fechaPublicacion = ?");
			pstmt.setString(1, c.getContenido());
			pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(c.getFecha()));
			pstmt.setInt(3, c.getBestia().getIdBestia());
			pstmt.setInt(4, c.getPublicador().getIdUsuario());
			pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(oldDate));
			int error = pstmt.executeUpdate();
			if(error == 0) {
				c = null;
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar comentario [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de comentario [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(c == null) {
			throw new DataNotFoundException("No se encontró el comentario para actualizar.");
		}
		return c;
	}
	
	public Comentario delete(Comentario c) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from comentario where idUsuario = ? and idBestia = ? and fechaPublicacion = ?");
			pstmt.setInt(1, c.getPublicador().getIdUsuario());
			pstmt.setInt(2, c.getBestia().getIdBestia());
			pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(c.getFecha()));
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar comentario [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de comentario [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return c;
	}
}
