package data;

import entities.Noticia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class DataNoticia {
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
				String estado = rs.getString("estado");
				LocalDateTime fechaPublicacion = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
				Integer idUsuario = rs.getInt("idUsuario");
				
				noticiaFound = new Noticia(id, titulo, contenido, estado, fechaPublicacion, idUsuario.toString());
			}
		} catch(SQLException ex) {
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
		};
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
					String estado = rs.getString("estado");
					LocalDateTime fechaPublicacion = rs.getTimestamp("fechaPublicacion").toLocalDateTime();
					Integer idUsuario = rs.getInt("idUsuario");
					
					noticiaFound = new Noticia(id, titulo, contenido, estado, fechaPublicacion, idUsuario.toString());
					noticias.add(noticiaFound);
				}
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
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
		return noticias;
	}

	public Noticia save(Noticia noticia) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into noticia(titulo, contenido, estado, fechaPublicacion, idUsuario) values (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, noticia.getTitulo());
			pstmt.setString(2, noticia.getContenido());
			pstmt.setString(3, noticia.getEstado());
			pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(noticia.getFechaPublicacion()));
			pstmt.setInt(5, Integer.parseInt(noticia.getIdUsuario()));
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				
				int id = rs.getInt(1);
				noticia.setId(id);
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
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
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
	            
			}
		}
		return noticia;
	}

	public Noticia update(Noticia noticiaNueva) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update noticia set contenido = ?, titulo = ?, estado = ?, fechaPublicacion = ?, idUsuario = ? where idNoticia = ?");
			pstmt.setString(1, noticiaNueva.getContenido());
			pstmt.setString(2, noticiaNueva.getTitulo());
			pstmt.setString(3, noticiaNueva.getEstado());
			pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(noticiaNueva.getFechaPublicacion()));
			pstmt.setInt(5, Integer.parseInt(noticiaNueva.getIdUsuario()));
			pstmt.setInt(6, noticiaNueva.getId());
			pstmt.executeUpdate();
		}catch(SQLException ex){
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
            noticiaNueva = null; //previsorio para caso que no encuentre un inv con idUsuario 
		} finally {
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
		return noticiaNueva;
	}

	public void delete(Noticia deletedNoticia) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from noticia where idNoticia = ?");
			pstmt.setInt(1, deletedNoticia.getId());
			pstmt.executeUpdate();
		}catch(SQLException ex){
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
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
