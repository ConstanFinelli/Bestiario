package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import entities.PasswordResetToken;

public class DataPasswordResetToken {
	
	public void save(PasswordResetToken token) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DbConnector.getInstancia().getConn().prepareStatement("INSERT INTO password_reset_token (token, idUsuario, expiration, used) VALUES (?, ?, ?, ?)");
	        ps.setString(1, token.getToken());
	        ps.setInt(2, token.getIdUsuario());
	        ps.setTimestamp(3, Timestamp.valueOf(token.getExpiration()));
	        ps.setBoolean(4, token.isUsed());
			ps.executeUpdate();
		}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			} finally {
				try {
					if(rs != null) {
						rs.close();
					}
					if(ps != null) {
						ps.close();
					}
					DbConnector.getInstancia().releaseConn();
				}catch(SQLException ex) {
					System.out.println("Mensaje: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
				}
			}
	}
	
	public PasswordResetToken getOne(String token) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		PasswordResetToken t = new PasswordResetToken();
		try {
			ps = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM password_reset_token WHERE token = ?");
			ps.setString(1, token);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {	
	            t.setToken(rs.getString("token"));
	            t.setIdUsuario(rs.getInt("idUsuario"));
	            t.setExpiration(rs.getTimestamp("expiration").toLocalDateTime());
	            t.setUsed(rs.getBoolean("used"));
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
				if(ps != null) {
					ps.close();
				}
				DbConnector.getInstancia().releaseConn();
			}catch(SQLException ex) {
				System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
			}
		}
		return t;
	} 
	
	public void markAsUsed(String token) {
		PreparedStatement ps = null;
		try {
			ps = DbConnector.getInstancia().getConn().prepareStatement("UPDATE password_reset_token SET used = true WHERE token = ?");
			ps.setString(1, token);
			
			ps.executeUpdate();
			
		}catch(SQLException ex) {
			System.out.println("Mensaje: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
		} finally {
			try {
				if(ps != null) {
					ps.close();
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
