package data;

import entities.Bestia;
import entities.Habitat;
import exceptions.DataNotFoundException;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHabitat {
	private static final Logger logger = Logger.getLogger(DataHabitat.class.getName());

	public Habitat getOne(Habitat ht) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Habitat htEncontrada = null;
		
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("SELECT * FROM habitat WHERE idHabitat=?");
			pstmt.setInt(1, ht.getId());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idHabitat");
				String nombre = rs.getString("nombre");
				String localizacion = rs.getString("localizacion");
				double latitud = rs.getDouble("latitud");
				double longitud = rs.getDouble("longitud");
				htEncontrada = new Habitat(id, nombre, localizacion, latitud, longitud);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al obtener habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(pstmt != null) {pstmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en getOne de habitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(htEncontrada == null) {
			throw new DataNotFoundException("No se encontró el hábitat con id " + ht.getId());
		}
		return htEncontrada;
	}
	
	public LinkedList<Habitat> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<Habitat> htsEncontradas = new LinkedList<>();
		Habitat htEncontrada = null;
		
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("SELECT * FROM habitat");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idHabitat");
					String nombre = rs.getString("nombre");
					String localizacion = rs.getString("localizacion");
					double latitud = rs.getDouble("latitud");
					double longitud = rs.getDouble("longitud");
					htEncontrada = new Habitat(id, nombre, localizacion, latitud, longitud);
					htsEncontradas.add(htEncontrada);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar habitats [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(stmt != null) {stmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAll de habitats [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return htsEncontradas;
	}
	
	public Habitat save(Habitat ht) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Habitat htGuardada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("INSERT INTO habitat(nombre, latitud, longitud, localizacion) VALUES (?,?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, ht.getNombre());
			pstmt.setDouble(2, ht.getLatitud());
			pstmt.setDouble(3, ht.getLongitud());
			pstmt.setString(4, ht.getLocalizacion());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				htGuardada = ht;
				htGuardada.setId(rs.getInt(1));
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en save de habitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return htGuardada;
	}
	
	public Habitat update(Habitat ht) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("UPDATE habitat SET nombre = ?, latitud = ?, longitud = ?, localizacion = ? WHERE idHabitat = ?");
			pstmt.setString(1, ht.getNombre());
			pstmt.setDouble(2, ht.getLatitud());
			pstmt.setDouble(3, ht.getLongitud());
			pstmt.setString(4, ht.getLocalizacion());
			pstmt.setInt(5, ht.getId());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				ht = null;
			}
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de habitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(ht == null) {
			throw new DataNotFoundException("No se encontró el hábitat a actualizar.");
		}
		return ht;
	}
	
	public Habitat delete(Habitat ht) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("DELETE FROM habitat WHERE idHabitat = ?");
			pstmt.setInt(1, ht.getId());
			pstmt.executeUpdate();
		} catch(SQLException ex){
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar habitat [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de habitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return ht;
	}
	
	public LinkedList<Habitat> findAllByBestia(Bestia b){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Habitat> habitatsBestia = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct h.idHabitat from habitat h join bestia_habitat bh on bh.idHabitat = h.idHabitat where bh.idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Habitat habitat = new Habitat(rs.getInt("idHabitat"));
					habitat = this.getOne(habitat);
					habitatsBestia.add(habitat);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar habitats por bestia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAllByBestia de habitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return habitatsBestia;
	}
}
