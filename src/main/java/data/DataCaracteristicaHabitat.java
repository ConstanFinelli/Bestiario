package data;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.CaracteristicaHabitat;
import entities.Habitat;
import exceptions.DataNotFoundException;

public class DataCaracteristicaHabitat {

	private static final Logger logger = Logger.getLogger(DataCaracteristicaHabitat.class.getName());

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
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar caracteristicas de habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(pstmt != null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAllById de caracteristica [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
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
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar caracteristica de habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en save de caracteristica [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return chS;
	}
	
	public CaracteristicaHabitat update(CaracteristicaHabitat ch, String newDescripcion) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("UPDATE caracteristica SET descripcion = ? WHERE idHabitat = ? AND descripcion = ?");
			pstmt.setString(1, newDescripcion);
			pstmt.setInt(2, ch.getIdHabitat());
			pstmt.setString(3, ch.getDescripcion());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				ch = null;
			}
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar caracteristica de habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de caracteristica [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(ch == null) {
			throw new DataNotFoundException("No se encontró la característica del hábitat para actualizar.");
		}
		return ch;
	}
	
	public CaracteristicaHabitat delete(CaracteristicaHabitat ch) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("DELETE FROM caracteristica WHERE idHabitat = ? AND descripcion = ?");
			pstmt.setInt(1, ch.getIdHabitat());
			pstmt.setString(2, ch.getDescripcion());
			pstmt.executeUpdate();
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar caracteristica de habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de caracteristica [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return ch;
	}
}
