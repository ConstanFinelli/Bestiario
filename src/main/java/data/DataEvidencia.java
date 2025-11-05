package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;

import entities.Bestia;
import entities.Evidencia;
import entities.TipoEvidencia;

public class DataEvidencia {
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
				LocalDateTime fechaO = rs.getTimestamp("fechaObtencion").toLocalDateTime();
				String estado = rs.getString("estado");
				String link = rs.getString("link");
				int idTipo = rs.getInt("idTipoEvidencia");
				TipoEvidencia tipo = teDao.getOne(new TipoEvidencia(idTipo, null));
				evidenciaEncontrada = new Evidencia(id, fechaO, estado, link, tipo);
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
		return evidenciaEncontrada;
	}
	
	public LinkedList<Evidencia> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		Evidencia evidencia = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from evidencia");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroEvidencia");
					LocalDateTime fechaO= rs.getTimestamp("fechaObtencion").toLocalDateTime();
					String estado = rs.getString("estado");
					String link = rs.getString("link");
					int idTipo = rs.getInt("idTipoEvidencia");
					TipoEvidencia tipo = teDao.getOne(new TipoEvidencia(idTipo, null));
					evidencia = new Evidencia(id, fechaO, estado, link, tipo);
					evidencias.add(evidencia);
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
		return evidencias;
	}
	
	public Evidencia save(Evidencia e) {
		asignarNroEvidencia(e);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into evidencia(nroEvidencia, fechaObtencion, estado, link, idTipoEvidencia) values(?, ?, ?, ? ,?)");
			pstmt.setInt(1, e.getNroEvidencia()); 
			pstmt.setTimestamp(2, java.sql.Timestamp.valueOf( e.getFechaObtencion()));
			pstmt.setString(3, e.getEstado());
			pstmt.setString(4, e.getLink());
			pstmt.setInt(5, e.getTipo().getId());
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
		return e;
		}
	
	public Evidencia update(Evidencia e) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update evidencia set fechaObtencion = ?, estado = ?, link = ? where idTipoEvidencia = ? and nroEvidencia = ?");
			pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(e.getFechaObtencion()));
			pstmt.setString(2, e.getEstado());
			pstmt.setString(3, e.getLink());
			pstmt.setInt(4,e.getTipo().getId());
			pstmt.setInt(5, e.getNroEvidencia());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				e = null;
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
		return e;
	}
	
	public Evidencia delete(Evidencia e) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from evidencia where idTipoEvidencia = ? and nroEvidencia = ?");
			pstmt.setInt(1, e.getTipo().getId());
			pstmt.setInt(2, e.getNroEvidencia());
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
	}
	
	public LinkedList<Evidencia> findAllType(TipoEvidencia te){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Evidencia evidencia = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from evidencia where idTipoEvidencia = ?");
			pstmt.setInt(1, te.getId());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroEvidencia");
					LocalDateTime fechaO= rs.getTimestamp("fechaObtencion").toLocalDateTime();
					String estado = rs.getString("estado");
					String link = rs.getString("link");
					evidencia = new Evidencia(id, fechaO, estado, link, te);
					evidencias.add(evidencia);
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
		return evidencias;
	}
	
	public LinkedList<Evidencia> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Evidencia evidencia = null;
		LinkedList<Evidencia> evidencias = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from evidencia ev inner join bestia_evidencia bv on ev.nroEvidencia = bv.nroEvidencia inner join bestia b on bv.idBestia = b.idBestia where b.idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int nroEvidencia = rs.getInt("nroEvidencia");
					LocalDateTime fechaO= rs.getTimestamp("fechaObtencion").toLocalDateTime();
					String estado = rs.getString("estado");
					String link = rs.getString("link");
					int idTipo = rs.getInt("idTipoEvidencia");
					TipoEvidencia te = teDao.getOne(new TipoEvidencia(idTipo, null));
					evidencia = new Evidencia(nroEvidencia,fechaO, estado, link, te);
					evidencias.add(evidencia);
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
		return evidencias;
	}
}

