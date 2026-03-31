package data;

import java.sql.*;
import java.time.LocalDateTime;
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
				String estado = rs.getString("estado");
				if("lector".equals(estado)) {
					LocalDateTime fechaNacimiento = rs.getTimestamp("fechaNacimiento").toLocalDateTime();
					usuarioEncontrado = new Lector(id, correo, contraseña, fechaNacimiento);
				}else {
					String dni = rs.getString("dni");
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					usuarioEncontrado = new Investigador(id, correo, contraseña, nombre, apellido, dni);
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
					String estado = rs.getString("estado");
					if("Investigador".equals(estado)) {
						String nombre = rs.getString("nombre");
						String apellido = rs.getString("apellido");
						String dni = rs.getString("dni");
						us = new Investigador(id, correo, contraseña, nombre, apellido, dni);
					}else {
						LocalDateTime fechaNacimiento = rs.getTimestamp("fechaNacimiento").toLocalDateTime();
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
			if(us.getEstado().equals("investigador")) {
				inv = (Investigador) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into usuario(correo,contraseña,nombre,apellido,dni,estado,recibirNotificaciones) values(?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, inv.getCorreo());
				pstmt.setString(2, inv.getContraseña());
				pstmt.setString(3, inv.getNombre());
				pstmt.setString(4, inv.getApellido());
				pstmt.setString(5, inv.getDni());
				pstmt.setString(6, inv.getEstado());
				pstmt.setBoolean(7, inv.getRecibirNotificaciones());
			}else {
				le = (Lector) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into usuario(correo,contraseña,fechaNacimiento,estado,recibirNotificaciones) values(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, le.getCorreo());
				pstmt.setString(2, le.getContraseña());
				pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(le.getFechaNacimiento()));
				pstmt.setString(4, le.getEstado());
				pstmt.setBoolean(5, le.getRecibirNotificaciones());
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
			if(!us.getEstado().equals("lector")) {
				inv = (Investigador) us;
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("update usuario set correo = ?, contraseña = ?, nombre = ?, apellido = ?, dni = ?, estado = ?, recibirNotificaciones = ? where idUsuario = ?");
				pstmt.setString(1, inv.getCorreo());
				pstmt.setString(2, inv.getContraseña());
				pstmt.setString(3, inv.getNombre());
				pstmt.setString(4, inv.getApellido());
				pstmt.setString(5, inv.getDni());
				pstmt.setString(6, inv.getEstado());
				pstmt.setInt(8, inv.getIdUsuario());
				pstmt.setBoolean(7, inv.getRecibirNotificaciones());
			}else {
				le = new Lector(us.getIdUsuario(), us.getCorreo(), us.getContraseña(), getFechaNacimiento(us.getIdUsuario()));
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("update usuario set correo = ?, contraseña = ?, fechaNacimiento = ?, estado = 'lector', recibirNotificaciones = ? where idUsuario = ?");
				pstmt.setString(1, le.getCorreo());
				pstmt.setString(2, le.getContraseña());
				pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(le.getFechaNacimiento()));
				pstmt.setInt(5, le.getIdUsuario());
				pstmt.setBoolean(4, le.getRecibirNotificaciones());
			}
			pstmt.executeUpdate();
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
            us = null;
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
	
	public Usuario getByEmail(String correo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Usuario us = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from usuario where correo = ?");
			pstmt.setString(1, correo);
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idUsuario");
				String contraseña = rs.getString("contraseña");
				String estado = rs.getString("estado");
				if(estado.equals("investigador")) {
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String dni = rs.getString("dni");
					us = new Investigador(id, correo, contraseña, nombre, apellido, dni);
				}else {
					LocalDateTime fechaNacimiento = rs.getTimestamp("fechaNacimiento").toLocalDateTime();
					us = new Lector(id, correo, contraseña, fechaNacimiento, estado);
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
		return us;
	}
	
	public LinkedList<Investigador> findAllSolicitantes() {
		Statement stmt = null;
		ResultSet rs = null;
		Investigador us = null;
		LinkedList<Investigador> usuarios = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from usuario where estado = 'solicitante'");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idUsuario");
					String correo = rs.getString("correo");
					String contraseña = rs.getString("contraseña");
					String estado = rs.getString("estado");
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String dni = rs.getString("dni");
					us = new Investigador(id, correo, contraseña, nombre, apellido, dni, estado);
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
	
	public LocalDateTime getFechaNacimiento(int id) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LocalDateTime fechaEncontrada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select fechaNacimiento from usuario where idUsuario = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				fechaEncontrada = rs.getTimestamp("fechaNacimiento").toLocalDateTime();
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
		return fechaEncontrada;
	}
	
	public LinkedList<Usuario>findByRecibirNotifcaciones() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Usuario us = null;
		LinkedList<Usuario> usuarios = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from usuario where recibirNotificaciones = true");
			
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idUsuario");
				String contraseña = rs.getString("contraseña");
				String estado = rs.getString("estado");
				String correo = rs.getString("correo");
				if(estado.equals("investigador")) {
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String dni = rs.getString("dni");
					us = new Investigador(id, correo, contraseña, nombre, apellido, dni);
				}else {
					LocalDateTime fechaNacimiento = rs.getTimestamp("fechaNacimiento").toLocalDateTime();
					us = new Lector(id, correo, contraseña, fechaNacimiento, estado);
				}
				usuarios.add(us);
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
		return usuarios;
	}
	
	public LinkedList<Investigador> getCorreosInvestigadoresYRecibNot(){
		Statement stmt = null;
		ResultSet rs = null;
		Investigador i = null;
		LinkedList<Investigador> investigadores= new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select correo, recibirNotificaciones from usuario where estado = 'investigador'");
			if(rs != null) {
				while(rs.next()) {
					i = new Investigador();
					
					i.setCorreo(rs.getString("correo"));
					i.setRecibirNotificaciones(rs.getBoolean("recibirNotificaciones"));
	
					investigadores.add(i);
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
		return investigadores;
	}
	
	public void updatePassword(int idUsuario, String pass) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update usuario set contraseña = ? where idUsuario = ?");
			pstmt.setString(1, pass);
			pstmt.setInt(2, idUsuario);
			
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
	}
}
