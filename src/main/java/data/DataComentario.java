package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import entities.*;

public class DataComentario {
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
					Usuario user = userDAO.getOne(new Usuario(idUsuario, null, null));
					comentario = new Comentario(user, bestia, fecha, contenido);
					comentarios.add(comentario);
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
		return c;
	}
}
