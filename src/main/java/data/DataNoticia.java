package data;

import entities.Investigador;
import entities.Noticia;
import exceptions.DataNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataNoticia {
	private static final Logger logger = Logger.getLogger(DataNoticia.class.getName());
	public DataUsuario userDAO = new DataUsuario();
	
	public Noticia getOne(Noticia noticia) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Noticia noticiaFound = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from noticia where idNoticia = ?");
			pstmt.setInt(1, noticia.getId());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idNoticia");
				String contenido = rs.getString("contenido");
				String titulo = rs.getString("titulo");
				LocalDateTime fechaPublicacion = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
				Investigador publicador = (Investigador) userDAO.getOne(new Investigador(rs.getInt("idUsuario")));
				
				noticiaFound = new Noticia(id, titulo, contenido, fechaPublicacion, publicador);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener noticia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getOne de noticia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(noticiaFound == null) {
			throw new DataNotFoundException("No se encontró la noticia solicitada.");
		}
		return noticiaFound;
	}
	
	public LinkedList<Noticia> findAll(){
		LinkedList<Noticia> noticias = new LinkedList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Noticia noticiaFound = null;
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from noticia");
			if(rs != null){
				while(rs.next()) {
					int id = rs.getInt("idNoticia");
					String contenido = rs.getString("contenido");
					String titulo = rs.getString("titulo");
					LocalDateTime fechaPublicacion = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
					Investigador publicador = (Investigador) userDAO.getOne(new Investigador(rs.getInt("idUsuario")));
					
					noticiaFound = new Noticia(id, titulo, contenido, fechaPublicacion, publicador);
					noticias.add(noticiaFound);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar noticias [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findAll de noticia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return noticias;
	}
	
	public LinkedList<Noticia> getUltimasNoticias(){
		LinkedList<Noticia> noticias = new LinkedList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Noticia noticiaFound = null;
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("select * from noticia order by fechaPublicacion desc limit 3");
			if(rs != null){
				while(rs.next()) {
					Integer id = rs.getInt("idNoticia");
					String contenido = rs.getString("contenido");
					String titulo = rs.getString("titulo");
					LocalDateTime fechaPublicacion = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
					Investigador publicador = (Investigador) userDAO.getOne(new Investigador(rs.getInt("idUsuario")));
					
					noticiaFound = new Noticia(id, titulo, contenido, fechaPublicacion, publicador);
					noticias.add(noticiaFound);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener ultimas noticias [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getUltimasNoticias [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return noticias;
	}

	public Noticia save(Noticia noticia) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into noticia(titulo, contenido, fechaPublicacion, idUsuario) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, noticia.getTitulo());
			pstmt.setString(2, noticia.getContenido());
			pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(noticia.getFechaPublicacion()));
			pstmt.setInt(4, noticia.getPublicador().getIdUsuario());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int id = rs.getInt(1);
				noticia.setId(id);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar noticia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
            noticia = null; //previsorio para caso que no encuentre un inv con idUsuario 
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
					"Error SQL al cerrar recursos en save de noticia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return noticia;
	}

	public Noticia update(Noticia noticiaNueva) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update noticia set contenido = ?, titulo = ?, fechaPublicacion = ?, idUsuario = ? where idNoticia = ?");
			pstmt.setString(1, noticiaNueva.getContenido());
			pstmt.setString(2, noticiaNueva.getTitulo());
			pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(noticiaNueva.getFechaPublicacion()));
			pstmt.setInt(4, noticiaNueva.getPublicador().getIdUsuario());
			pstmt.setInt(5, noticiaNueva.getId());
			pstmt.executeUpdate();
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar noticia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
            noticiaNueva = null; //previsorio para caso que no encuentre un inv con idUsuario 
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de noticia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}		
		if(noticiaNueva == null) {
			throw new DataNotFoundException("No se encontró la noticia para actualizar.");
		}
		return noticiaNueva;
	}

	public void delete(Noticia deletedNoticia) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from noticia where idNoticia = ?");
			pstmt.setInt(1, deletedNoticia.getId());
			pstmt.executeUpdate();
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar noticia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de noticia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
}
