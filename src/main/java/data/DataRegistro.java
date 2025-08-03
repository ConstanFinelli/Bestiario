package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;

import entities.Registro;
import entities.Bestia;
import entities.Usuario;
import entities.Investigador;
import logic.LogicUsuario;
import logic.LogicBestia;

public class DataRegistro {
	public LogicUsuario controladorUsuarios = new LogicUsuario();
	public LogicBestia controladorBestia = new LogicBestia();
	
	public Registro getOne(Registro r) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Registro registroEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from registro where nroRegistro = ? and idBestia = ?");
			pstmt.setInt(1, r.getNroRegistro());
			pstmt.setInt(2, r.getBestia().getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("nroRegistro");
				String detalles = rs.getString("detalles");
				LocalDate fechaA= rs.getDate("fechaAprobacion").toLocalDate();
				LocalDate fechaB = rs.getDate("fechaBaja").toLocalDate();
				Investigador pub = (Investigador) controladorUsuarios.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
				String estado = rs.getString("estado");
				Bestia bestia = controladorBestia.getOne(new Bestia(rs.getInt("idBestia"), null, null));
				registroEncontrado = new Registro(id, detalles, fechaA, fechaB, pub, estado, bestia);
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
		return registroEncontrado;
	}
	
	public LinkedList<Registro> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		Registro registro = null;
		LinkedList<Registro> registros = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from Registro");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroRegistro");
					String detalles = rs.getString("detalles");
					LocalDate fechaA= rs.getDate("fechaAprobacion").toLocalDate();
					LocalDate fechaB = rs.getDate("fechaBaja").toLocalDate();
					Investigador pub = (Investigador) controladorUsuarios.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
					String estado = rs.getString("estado");
					Bestia bestia = controladorBestia.getOne(new Bestia(rs.getInt("idBestia"), null, null));
					registro = new Registro(id, detalles, fechaA, fechaB, pub, estado, bestia);
					registros.add(registro);
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
		return registros;
	}
	
	public Registro save(Registro r) {
		asignarNroRegistro(r);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into Registro(nroRegistro, detalles, fechaAprobacion, fechaBaja, Publicador, estado, idBestia) values(?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, r.getNroRegistro());
			pstmt.setString(2, r.getDetalles());
			pstmt.setDate(3, java.sql.Date.valueOf(r.getFechaAprobacion()));
			pstmt.setDate(4, java.sql.Date.valueOf(r.getFechaBaja()));
			pstmt.setInt(5, r.getPublicador().getIdUsuario());
			pstmt.setString(6, r.getEstado());
			pstmt.setInt(7, r.getBestia().getIdBestia());
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
		return r;
		}
	
	public Registro update(Registro r) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update Registro set detalles = ?, fechaAprobacion = ?, fechaBaja = ?, Publicador = ?. estado = ? where idBestia = ? and nroRegistro = ?");
			pstmt.setString(1, r.getDetalles());
			pstmt.setDate(2, java.sql.Date.valueOf(r.getFechaAprobacion()));
			pstmt.setDate(3, java.sql.Date.valueOf(r.getFechaBaja()));
			pstmt.setInt(4, r.getPublicador().getIdUsuario());
			pstmt.setString(5, r.getEstado());
			pstmt.setInt(6, r.getBestia().getIdBestia());
			pstmt.setInt(7, r.getNroRegistro());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				r = null;
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
		return r;
	}
	
	public Registro delete(Registro r) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from registro where idBestia = ? and nroRegistro = ?");
			pstmt.setInt(1, r.getBestia().getIdBestia());
			pstmt.setInt(2, r.getNroRegistro());
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
		return r;
	}
	
	public void asignarNroRegistro(Registro r) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select max(nroEvidencia) as maxnumber from registro where idBestia = ?");
			pstmt.setInt(1, r.getBestia().getIdBestia());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				r.setNroRegistro(rs.getInt("maxnumber") + 1);
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
}
