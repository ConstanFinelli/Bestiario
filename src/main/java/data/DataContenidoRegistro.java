package data;

import java.sql.*;

import entities.ContenidoRegistro;


public class DataContenidoRegistro {
	public ContenidoRegistro getOne(int idContenido) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ContenidoRegistro cr = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from contenidosRegistros where idContenido = ?");
			pstmt.setInt(1, idContenido);
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = idContenido;
				String introduccion = rs.getString("introduccion");
				String historia = rs.getString("historia");
				String descripcion = rs.getString("descripcion");
				String resumen = rs.getString("resumen");
				cr = new ContenidoRegistro(id, introduccion, historia, descripcion, resumen);
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
			try {
				if(rs != null) {rs.close();}
				if(pstmt != null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return cr;
	}
	
	public ContenidoRegistro save(ContenidoRegistro cr) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into contenidoRegistros(introduccion,historia, descripcion, resumen) values(?, ?, ?, ?)");
			pstmt.setString(1, cr.getIntroduccion()); 
			pstmt.setString(2, cr.getHistoria());
			pstmt.setString(3, cr.getDescripcion());
			pstmt.setString(4, cr.getResumen());
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
		return cr;
	}
	
	public ContenidoRegistro update(ContenidoRegistro cr) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update contenidoRegistro set introduccion = ?, historia = ?, descripcion = ?, resumen = ? where idContenido = ?");
			pstmt.setString(1, cr.getIntroduccion());
			pstmt.setString(2, cr.getHistoria());
			pstmt.setString(3, cr.getDescripcion());
			pstmt.setString(4, cr.getResumen());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				cr = null;
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
		return cr;
	}
}
