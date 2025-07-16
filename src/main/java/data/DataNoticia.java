package data;

import entities.Noticia;

import java.sql.*;
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
				Date fechaPublicacion = rs.getDate("fechaPublicacion");
				int idUsuario = rs.getInt("idUsuario");
				noticiaFound = new Noticia(id, titulo, contenido, estado, fechaPublicacion, idUsuario);
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
					Date fechaPublicacion = rs.getDate("fechaPublicacion");
					int idUsuario = rs.getInt("idUsuario");
					noticiaFound = new Noticia(id, titulo, contenido, estado, fechaPublicacion, idUsuario);
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
}
