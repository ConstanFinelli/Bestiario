package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import entities.Registro;
import entities.Bestia;
import entities.ContenidoRegistro;
import entities.Usuario;
import entities.Investigador;

public class DataRegistro {
	public DataUsuario userDAO = new DataUsuario();
	public DataContenidoRegistro cRDAO = new DataContenidoRegistro();
	
	public Registro getRegistroToShow(Bestia b, LocalDate fecha) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Registro registroEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from registro where idBestia = ? and fechaAprobacion <= ? and (fechaBaja is null or fechaBaja > ?) and estado = ? order by fechaAprobacion desc limit 1");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.setDate(2, java.sql.Date.valueOf(fecha));
			pstmt.setDate(3, java.sql.Date.valueOf(fecha));
			pstmt.setString(4, "aprobado");
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("nroRegistro");
				LocalDate fechaA = null;
				LocalDate fechaB = null;
				String mainPic = rs.getString("main_picture");
				ContenidoRegistro contenido = cRDAO.getOne(Integer.parseInt(rs.getString("idContenido")));
				if(rs.getDate("fechaAprobacion") != null) {
					fechaA= rs.getDate("fechaAprobacion").toLocalDate();
				}
				if(rs.getDate("fechaBaja") != null) {
				fechaB = rs.getDate("fechaBaja").toLocalDate();
				}
				Investigador pub = (Investigador) userDAO.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
				String estado = rs.getString("estado");
				Bestia bestia = b;
				registroEncontrado = new Registro(id, mainPic, contenido, fechaA, fechaB, pub, estado, bestia);
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
	
	public Registro getOne(Registro r) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Registro registroEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from registro where idBestia = ? and nroRegistro = ?");
			pstmt.setInt(1, r.getBestia().getIdBestia());
			pstmt.setInt(2, r.getNroRegistro());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				ContenidoRegistro contenido = cRDAO.getOne(Integer.parseInt(rs.getString("idContenido")));
				LocalDate fechaA = null;
				LocalDate fechaB = null;
				String mainPic = rs.getString("main_picture");
				if(rs.getDate("fechaAprobacion") != null) {
					fechaA= rs.getDate("fechaAprobacion").toLocalDate();
				}
				if(rs.getDate("fechaBaja") != null) {
					fechaB = rs.getDate("fechaBaja").toLocalDate();
				}
				Investigador pub = (Investigador) userDAO.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
				String estado = rs.getString("estado");
				Bestia bestia = r.getBestia();
				registroEncontrado = new Registro(r.getNroRegistro(), mainPic, contenido, fechaA, fechaB, pub, estado, bestia);
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
	
	public LinkedList<Registro> findRegistrosPendientes(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Registro registro = null;
		LinkedList<Registro> registros = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from Registro where idBestia = ? and estado = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.setString(2, "pendiente");
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroRegistro");
					LocalDate fechaA = null;
					LocalDate fechaB = null;
					String mainPic = rs.getString("main_picture");
					ContenidoRegistro contenido = cRDAO.getOne(Integer.parseInt(rs.getString("idContenido")));
					if(rs.getDate("fechaAprobacion") != null) {
						fechaA= rs.getDate("fechaAprobacion").toLocalDate();
					}
					if(rs.getDate("fechaBaja") != null) {
					fechaB = rs.getDate("fechaBaja").toLocalDate();
					}
					Investigador pub = (Investigador) userDAO.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
					String estado = rs.getString("estado");
					Bestia bestia = b;
					registro = new Registro(id, mainPic, contenido, fechaA, fechaB, pub, estado, bestia);
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
		return registros;
	}
	
	public LinkedList<Registro> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Registro registro = null;
		LinkedList<Registro> registros = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select * from Registro where idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("nroRegistro");
					LocalDate fechaA = null;
					LocalDate fechaB = null;
					String mainPic = rs.getString("main_picture");
					ContenidoRegistro contenido = cRDAO.getOne(Integer.parseInt(rs.getString("idContenido")));
					if(rs.getDate("fechaAprobacion") != null) {
						fechaA= rs.getDate("fechaAprobacion").toLocalDate();
					}
					if(rs.getDate("fechaBaja") != null) {
					fechaB = rs.getDate("fechaBaja").toLocalDate();
					}
					Investigador pub = (Investigador) userDAO.getOne(new Usuario(rs.getInt("idUsuario"), null, null));
					String estado = rs.getString("estado");
					Bestia bestia = b;
					registro = new Registro(id, mainPic, contenido, fechaA, fechaB, pub, estado, bestia);
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
		return registros;
	}
	
	public Registro save(Registro r) {
		asignarNroRegistro(r);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into Registro(nroRegistro, main_picture, idContenido, fechaAprobacion, fechaBaja, idUsuario, estado, idBestia) values(?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, r.getNroRegistro());
			pstmt.setString(2, r.getMainPic());
			pstmt.setInt(3, r.getContenido().getIdContenido());
			pstmt.setDate(4, (r.getFechaAprobacion() == null) ? null:java.sql.Date.valueOf(r.getFechaAprobacion()));
			pstmt.setDate(5, (r.getFechaBaja() == null) ? null:java.sql.Date.valueOf(r.getFechaBaja()));
			if(r.getPublicador() == null) {
				pstmt.setNull(6, java.sql.Types.INTEGER);
			}else {
				pstmt.setInt(6, r.getPublicador().getIdUsuario());
			}			
			pstmt.setString(7, r.getEstado());
			pstmt.setInt(8, r.getBestia().getIdBestia());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update Registro set main_picture = ?, contenido_registro = ?, fechaAprobacion = ?, fechaBaja = ?, Publicador = ?, estado = ? where idBestia = ? and nroRegistro = ?");
			pstmt.setString(1, r.getMainPic());
			pstmt.setInt(2, r.getContenido().getIdContenido());
			pstmt.setDate(3, java.sql.Date.valueOf(r.getFechaAprobacion()));
			pstmt.setDate(4, java.sql.Date.valueOf(r.getFechaBaja()));
			pstmt.setInt(5, r.getPublicador().getIdUsuario());
			pstmt.setString(6, r.getEstado());
			pstmt.setInt(7, r.getBestia().getIdBestia());
			pstmt.setInt(8, r.getNroRegistro());
			cRDAO.update(r.getContenido());
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
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select max(nroRegistro) as maxnumber from registro where idBestia = ?");
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
	
	public Registro updateEstado(Registro r) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update Registro set fechaAprobacion = ?, idUsuario = ?, estado = ? where idBestia = ? and nroRegistro = ?");
			pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
			pstmt.setInt(2, r.getPublicador().getIdUsuario());
			pstmt.setString(3, "aprobado");
			pstmt.setInt(4, r.getBestia().getIdBestia());
			pstmt.setInt(5, r.getNroRegistro());
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
	
	public String obtenerNombreImagen(String idBestia) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nroRegistro = 0;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select max(nroRegistro) as nroRegistro from registro where idBestia = ?");
			pstmt.setString(1, idBestia);
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				nroRegistro = rs.getInt("nroRegistro") + 1;
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
		return(idBestia + "_" + nroRegistro);
	}
	
}
