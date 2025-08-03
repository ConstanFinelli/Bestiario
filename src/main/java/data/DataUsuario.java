package data;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

import entities.Usuario;
import entities.Investigador;
import entities.Lector;

public class DataUsuario {
	public Usuario getOne(Usuario usB) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Usuario usuarioEncontrado = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from usuario where idUsuario = ?");
			pstmt.setInt(1, usB.getIdUsuario());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idUsuario");
				String correo = rs.getString("correo");
				String contraseña = rs.getString("contraseña");
				boolean esInv = rs.getBoolean("esInvestigador");
				if(esInv) {
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String dni = rs.getString("dni");
					usuarioEncontrado = new Investigador(id, correo, contraseña, nombre, apellido, dni);
				}else {
					LocalDate fechaNacimiento = rs.getDate("fechaNacimiento").toLocalDate();
					usuarioEncontrado = new Lector(id, correo, contraseña, fechaNacimiento);
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
		return usuarioEncontrado;
	}
	
	public LinkedList<Usuario> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		Usuario us = null;
		LinkedList<Usuario> usuarios = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from usuario");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idUsuario");
					String correo = rs.getString("correo");
					String contraseña = rs.getString("contraseña");
					boolean esInv = rs.getBoolean("esInvestigador");
					if(esInv) {
						String nombre = rs.getString("nombre");
						String apellido = rs.getString("apellido");
						String dni = rs.getString("dni");
						us = new Investigador(id, correo, contraseña, nombre, apellido, dni);
					}else {
						LocalDate fechaNacimiento = rs.getDate("fechaNacimiento").toLocalDate();
						us = new Lector(id, correo, contraseña, fechaNacimiento);
					}
					usuarios.add(us);
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
		return usuarios;
	}
	
	
	
	public Usuario save(Usuario us) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Investigador inv = null;
		Lector le = null;
		if(us == null) {return us;}
		try {
			if(us.isEsInvestigador()) {
				inv = (Investigador) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into usuario(correo,contraseña,nombre,apellido,dni,esInvestigador) values(?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, inv.getCorreo());
				pstmt.setString(2, inv.getContraseña());
				pstmt.setString(3, inv.getNombre());
				pstmt.setString(4, inv.getApellido());
				pstmt.setString(5, inv.getDni());
				pstmt.setBoolean(6, true);
			}else {
				le = (Lector) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into usuario(correo,contraseña,fechaNacimiento,esInvestigador) values(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, le.getCorreo());
				pstmt.setString(2, le.getContraseña());
				pstmt.setDate(3, java.sql.Date.valueOf(le.getFechaNacimiento()));
				pstmt.setBoolean(4, false);
			}
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				if(inv != null) {
					inv.setIdUsuario(rs.getInt(1));
				}else {
				le.setIdUsuario(rs.getInt(1));
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
		if(inv != null) {
		return inv;
		}else {return le;}
		}
	
	public Usuario update(Usuario us) {
		PreparedStatement pstmt = null;
		Investigador inv = null;
		Lector le = null;
		if(us == null) {return us;}
		try {
			if(us.isEsInvestigador()) {
				inv = (Investigador) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("update usuario set correo = ?, contraseña = ?, nombre = ?, apellido = ?, dni = ? where idUsuario = ?");
				pstmt.setString(1, inv.getCorreo());
				pstmt.setString(2, inv.getContraseña());
				pstmt.setString(3, inv.getNombre());
				pstmt.setString(4, inv.getApellido());
				pstmt.setString(5, inv.getDni());
			}else {
				le = (Lector) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("update usuario set correo = ?, contraseña = ?, fechaNacimiento = ? where idUsuario = ?");
				pstmt.setString(1, le.getCorreo());
				pstmt.setString(2, le.getContraseña());
				pstmt.setDate(3, java.sql.Date.valueOf(le.getFechaNacimiento()));
			}
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
		return us;
	}
	
	public Usuario delete(Usuario usBorrado) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from usuario where idUsuario = ?");
			pstmt.setInt(1, usBorrado.getIdUsuario());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				usBorrado = null;
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
		return usBorrado;
	}
	
}
