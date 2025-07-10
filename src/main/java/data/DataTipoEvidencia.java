package data;

import entities.TipoEvidencia;
import java.sql.*;
import java.util.LinkedList;

public class DataTipoEvidencia {

	public TipoEvidencia getOne(TipoEvidencia tipoE) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TipoEvidencia tipoEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from tipo_evidencia where id = ?");
			pstmt.setInt(1, tipoE.getId());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("id");
				String descripcion = rs.getString("descripcion");
				tipoEncontrado = new TipoEvidencia(id, descripcion);
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
					int id = rs.getInt("id");
					String descripcion = rs.getString("descripcion");
					tipoEncontrado = new TipoEvidencia(id, descripcion);
					tipos.add(tipoEncontrado);
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
		return tipos;
	} 
	
	public TipoEvidencia save(TipoEvidencia tipoS) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into tipo_evidencia values (?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, tipoS.getDescripcion());
			pstmt.executeQuery();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int id = rs.getInt("id");
				tipoS.setId(id);
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
		return tipoS;
	}
	
	public TipoEvidencia update(TipoEvidencia datos) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update tipo_evidencia set descripcion = ? where id = ?");
			pstmt.setString(1, datos.getDescripcion());
			pstmt.setInt(2, datos.getId());
			pstmt.executeQuery();
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
		return datos;
	}
	
	public TipoEvidencia delete(TipoEvidencia datoBorrado) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from tipo_evidencia where id = ?");
			pstmt.setInt(1, datoBorrado.getId());
			pstmt.executeQuery();
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
		return datoBorrado;
	}
}
