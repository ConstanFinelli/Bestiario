package data;

import java.sql.*;
import java.util.LinkedList;

import entities.CaracteristicaHabitat;
import entities.Habitat;

public class DataCaracteristicaHabitat {
	public LinkedList<CaracteristicaHabitat> findAllById(Habitat ht){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int idHab = ht.getId();
		LinkedList<CaracteristicaHabitat> caracteristicas = new LinkedList<>();
		
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM caracteristica WHERE idHabitat=?");
			pstmt.setInt(1, idHab);
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					CaracteristicaHabitat car = new CaracteristicaHabitat(idHab, rs.getString("descripcion"));
					caracteristicas.add(car);
				}
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
		return caracteristicas;
	}
	
	public CaracteristicaHabitat save(CaracteristicaHabitat ch,Habitat ht) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int idHab = ht.getId();
		CaracteristicaHabitat chS = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("INSERT INTO caracteristica(idHabitat, descripcion) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, idHab);
			pstmt.setString(2, ch.getDescripcion());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				chS = ch;
				chS.setIdHabitat(idHab);
			}
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		}finally {
			try {
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return chS;
	}
	
	public CaracteristicaHabitat update(CaracteristicaHabitat ch) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("UPDATE caracteristica SET descripcion = ? WHERE idHabitat = ?");
			pstmt.setString(1, ch.getDescripcion());
			pstmt.setInt(2, ch.getIdHabitat());
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
		return ch;
	}
	
	public CaracteristicaHabitat delete(CaracteristicaHabitat ch) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("DELETE FROM caracteristica WHERE idHabitat = ?, descripcion = ?");
			pstmt.setInt(1, ch.getIdHabitat());
			pstmt.setString(2, ch.getDescripcion());
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
		return ch;
	}
}
