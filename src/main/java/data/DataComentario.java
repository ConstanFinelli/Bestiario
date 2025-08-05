package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;

import entities.*;
import logic.*;

public class DataComentario {
	public LogicBestia controladorBestia = new LogicBestia();
	public LogicUsuario controladorUsuario = new LogicUsuario();
	
	public Comentario getOne(Comentario c) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comentario comentarioEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from comentario where idUsuario = ?, idBestia = ?, fechaPublicacion = ?");
			pstmt.setInt(1, c.getPublicador().getIdUsuario());
			pstmt.setInt(2, c.getBestia().getIdBestia());
			pstmt.setDate(3, java.sql.Date.valueOf(c.getFecha()));
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
	
	public LinkedList<Comentario> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		Comentario comentario = null;
		LinkedList<Comentario> comentarios = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from comentario");
			if(rs != null) {
				while(rs.next()) {
					int idBestia = rs.getInt("idBestia");
					int idUsuario = rs.getInt("IdUsuario");
					String contenido = rs.getString("contenido");
					LocalDate fecha = rs.getDate("fechaPublicacion").toLocalDate();
					Bestia bestia = controladorBestia.getOne(new Bestia(idBestia, null, null));
					Usuario user = controladorUsuario.getOne(new Usuario(idUsuario, null, null));
					comentario = new Comentario(user, bestia, fecha, contenido);
					comentarios.add(comentario);
					;
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
			pstmt.setDate(4, java.sql.Date.valueOf(c.getFecha()));
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
	
	public Comentario update(Comentario c, LocalDate oldDate) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update comentario set contenido = ?, fechaPublicacion = ? where idBestia = ? and idUsuario = ? and fechaPublicacion = ?");
			pstmt.setString(1, c.getContenido());
			pstmt.setDate(2, java.sql.Date.valueOf(c.getFecha()));
			pstmt.setInt(3, c.getBestia().getIdBestia());
			pstmt.setInt(4, c.getPublicador().getIdUsuario());
			pstmt.setDate(5, java.sql.Date.valueOf(oldDate));
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
			pstmt.setDate(3, java.sql.Date.valueOf(c.getFecha()));
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
