package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.PasswordResetToken;

public class DataPasswordResetToken {

	private static final Logger logger = Logger.getLogger(DataPasswordResetToken.class.getName());
	
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
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar token de reseteo de contraseña [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en save de token [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public PasswordResetToken getOne(String token) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		PasswordResetToken t = null;
		try {
			ps = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM password_reset_token WHERE token = ?");
			ps.setString(1, token);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				t = new PasswordResetToken();
	            t.setToken(rs.getString("token"));
	            t.setIdUsuario(rs.getInt("idUsuario"));
	            t.setExpiration(rs.getTimestamp("expiration").toLocalDateTime());
	            t.setUsed(rs.getBoolean("used"));
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener token de reseteo de contraseña [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en getOne de token [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
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
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al marcar token como usado [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en markAsUsed de token [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
    }
	
	public void deleteByUser(int idUser) {
		PreparedStatement ps = null;
		try {
			ps = DbConnector.getInstancia().getConn().prepareStatement("DELETE FROM password_reset_token WHERE idUsuario = ?");
			ps.setInt(1, idUser);
			ps.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar token por usuario [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en deleteByUser de token [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
}
