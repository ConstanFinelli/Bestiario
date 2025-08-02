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
	
	/*
	
	public Categoria save(Categoria cat) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into categoria(descripcion) values(?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cat.getDescripcionCategoria());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				cat.setIdCategoria(rs.getInt(1));
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
		return cat;
		}
	
	public Categoria update(Categoria cat) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update categoria set descripcion = ? where idCategoria = ?");
			pstmt.setString(1, cat.getDescripcionCategoria());
			pstmt.setInt(2, cat.getIdCategoria());
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
		return cat;
	}
	
	public Categoria delete(Categoria catBorrada) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from categoria where idCategoria = ?");
			pstmt.setInt(1, catBorrada.getIdCategoria());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				cat = null;
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
		return catBorrada;
	}
	*/
}
