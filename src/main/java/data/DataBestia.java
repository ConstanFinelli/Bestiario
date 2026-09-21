package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Categoria;
import entities.Evidencia;
import entities.Habitat;
import entities.Registro;
import exceptions.DataNotFoundException;

//INCOMPLETO: DEBATIR EN GRUPO COMO PROSEGUIR Y ORGANIZAR EL TEMA DE CATEGORIAS, HABITATS Y REGISTROS EN EL SAVE
public class DataBestia {
	private static final Logger logger = Logger.getLogger(DataBestia.class.getName());
	public DataHabitat habDAO = new DataHabitat();
	public DataCategoria catDAO = new DataCategoria();
	public DataRegistro regDAO = new DataRegistro();
	public DataComentario comDAO = new DataComentario();
	public DataEvidencia evDAO = new DataEvidencia();
	
	public Bestia getOne(Bestia b) {
		if(b == null) {
			throw new DataNotFoundException("No se proporcionó una bestia para buscar.");
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Bestia bestiaEncontrada = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select * from bestia where idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			rs = pstmt.executeQuery();
			if(rs != null && rs.next()) {
				int id = rs.getInt("idBestia");
				String nombre = rs.getString("nombre");
				String peligrosidad = rs.getString("peligrosidad");
				String estado = rs.getString("estado");
				bestiaEncontrada = new Bestia(id, nombre, peligrosidad, estado);
				completarBestia(bestiaEncontrada);
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al consultar bestia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en getOne de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		if(bestiaEncontrada == null) {
			throw new DataNotFoundException("No se encontró la bestia con id " + b.getIdBestia());
		}
		return bestiaEncontrada;
	}
	
	public LinkedList<Bestia> findAll(){
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<Bestia> bestias = new LinkedList<>();
		try {
			stmt = DbConnector.getInstancia().getConn().createStatement();
			rs = stmt.executeQuery("Select * from bestia order by nombre asc");
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idBestia");
					String nombre = rs.getString("nombre");
					String peligrosidad = rs.getString("peligrosidad");
					String estado = rs.getString("estado");
					Bestia bestia = new Bestia(id, nombre, peligrosidad, estado);
					completarBestia(bestia);
					bestias.add(bestia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar bestias [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en findAll de bestias [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return bestias;
	}
	
	public LinkedList<Bestia> findByCategoria (String categoria){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Bestia> bestias = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("Select b.* from bestia b "
					+ "inner join bestia_categoria bc on bc.idBestia = b.idBestia"
					+ " inner join categoria c on c.idCategoria = bc.idCategoria "
					+ "where c.nombre = ? order by b.nombre asc");
			pstmt.setString(1, categoria);
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					int id = rs.getInt("idBestia");
					String nombre = rs.getString("nombre");
					String peligrosidad = rs.getString("peligrosidad");
					String estado = rs.getString("estado");
					Bestia bestia = new Bestia(id, nombre, peligrosidad, estado);
					completarBestia(bestia);
					bestias.add(bestia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al buscar bestias por categoria [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en findByCategoria de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return bestias;
	}
	
	public Bestia save(Bestia b) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into Bestia(nombre, peligrosidad, estado) values(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, b.getNombre());
			pstmt.setString(2, b.getPeligrosidad());
			pstmt.setString(3, b.getEstado());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs != null && rs.next()) {
				b.setIdBestia(rs.getInt(1));
			}
			saveCategorias(b);
			saveRegistros(b);
			saveHabitats(b);
			saveEvidencias(b);
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al guardar bestia [SQLState: %s, ErrorCode: %d]: %s",
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
					"Error SQL al cerrar recursos en save de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return b;
	}
	
	public Bestia update(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update Bestia set nombre = ?, peligrosidad = ?, estado = ? where idBestia = ?");
			pstmt.setString(1, b.getNombre());
			pstmt.setString(2, b.getPeligrosidad());
			pstmt.setString(3, b.getEstado());
			pstmt.setInt(4, b.getIdBestia());
			int error = pstmt.executeUpdate();
			if(error == 0) {
				b = null;
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al actualizar bestia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en update de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return b;
	}
	
	public Bestia delete(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.executeUpdate();
			if(b.getCategorias() != null) {
				deleteCategorias(b);
			}
			if(b.getHabitats() != null) {
				deleteHabitats(b);
			}
			//preguntar si se deberian eliminar las bestias
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar bestia [SQLState: %s, ErrorCode: %d]: %s",
				ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en delete de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return b;
	}
	
	public void addCategorias(Bestia bestiaEncontrada){
		bestiaEncontrada.setCategorias(catDAO.findAllByBestia(bestiaEncontrada));
	}
	public void addHabitats(Bestia bestiaEncontrada) {
		bestiaEncontrada.setHabitats(habDAO.findAllByBestia(bestiaEncontrada));
	}
	public void addRegistros(Bestia bestiaEncontrada) {
		bestiaEncontrada.setRegistros(regDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void addComentarios(Bestia bestiaEncontrada) {
		bestiaEncontrada.setComentarios(comDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void addEvidencias(Bestia bestiaEncontrada) {
		bestiaEncontrada.setEvidencias(evDAO.findAllByBestia(bestiaEncontrada));
	}
	
	public void completarBestia(Bestia bestia) {
		addRegistros(bestia);
		addHabitats(bestia);
		addCategorias(bestia);
		addComentarios(bestia);
		addEvidencias(bestia);
	}
	
	public void saveCategorias(Bestia b) {
		PreparedStatement pstmt = null;
		for(Categoria categoria : b.getCategorias()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_categoria(idBestia, idCategoria) values(?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, categoria.getIdCategoria());
				pstmt.executeUpdate();
			} catch(SQLException ex){
				logger.log(Level.SEVERE, String.format(
					"Error SQL al asociar categoria %d a bestia %d [SQLState: %s, ErrorCode: %d]: %s",
					categoria.getIdCategoria(), b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			} finally {
				try {
					if(pstmt != null) {
						pstmt.close();
					}
					DbConnector.getInstancia().releaseConn();
				} catch(SQLException ex) {
					logger.log(Level.SEVERE, String.format(
						"Error SQL al cerrar recursos en saveCategorias de bestia [SQLState: %s, ErrorCode: %d]: %s",
						ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
				}
			}
		}	
	}
	
	public void saveHabitats(Bestia b) {
		PreparedStatement pstmt = null;
		for(Habitat habitat : b.getHabitats()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_habitat(idBestia, idHabitat) values(?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, habitat.getId());
				pstmt.executeUpdate();
			} catch(SQLException ex){
				logger.log(Level.SEVERE, String.format(
					"Error SQL al asociar habitat %d a bestia %d [SQLState: %s, ErrorCode: %d]: %s",
					habitat.getId(), b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			} finally {
				try {
					if(pstmt != null) {
						pstmt.close();
					}
					DbConnector.getInstancia().releaseConn();
				} catch(SQLException ex) {
					logger.log(Level.SEVERE, String.format(
						"Error SQL al cerrar recursos en saveHabitats de bestia [SQLState: %s, ErrorCode: %d]: %s",
						ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
				}
			}
		}	
	}
	
	public void saveEvidencias(Bestia b) {
		PreparedStatement pstmt = null;
		for(Evidencia evidencia : b.getEvidencias()) {
			try {
				pstmt = DbConnector.getInstancia().getConn().prepareStatement("insert into bestia_evidencia(idBestia, nroEvidencia, idTipoEvidencia ,detalle) values(?,?,?,?)");
				pstmt.setInt(1, b.getIdBestia());
				pstmt.setInt(2, evidencia.getNroEvidencia());
				pstmt.setInt(3, evidencia.getTipo().getId());
				pstmt.setString(4, "Detalle a actualizar");
				pstmt.executeUpdate();
			} catch(SQLException ex){
				logger.log(Level.SEVERE, String.format(
					"Error SQL al asociar evidencia %d-%d a bestia %d [SQLState: %s, ErrorCode: %d]: %s",
					evidencia.getTipo().getId(), evidencia.getNroEvidencia(), b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			} finally {
				try {
					if(pstmt != null) {
						pstmt.close();
					}
					DbConnector.getInstancia().releaseConn();
				} catch(SQLException ex) {
					logger.log(Level.SEVERE, String.format(
						"Error SQL al cerrar recursos en saveEvidencias de bestia [SQLState: %s, ErrorCode: %d]: %s",
						ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
				}
			}
		}	
	}
	
	public void saveRegistros(Bestia b) {
		for(Registro registro : b.getRegistros()) {
			regDAO.save(registro);
		}
	}
	
	public void deleteCategorias(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_categoria where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar categorias de bestia %d [SQLState: %s, ErrorCode: %d]: %s",
				b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en deleteCategorias de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public void deleteHabitats(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_habitat where idbestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al eliminar habitats de bestia %d [SQLState: %s, ErrorCode: %d]: %s",
				b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en deleteHabitats de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public LinkedList<Bestia> findAllBestiasFromHabitat(Habitat ht){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedList<Bestia> bestiasHabitat = new LinkedList<>();
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("select distinct * from habitat h join bestia_habitat bh on bh.idHabitat = h.idHabitat where bh.idHabitat = ?");
			pstmt.setInt(1, ht.getId());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					Bestia bestia = new Bestia(rs.getInt("idBestia"), null, null, null);
					bestia = this.getOne(bestia);
					bestiasHabitat.add(bestia);
				}
			}
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al listar bestias del habitat %d [SQLState: %s, ErrorCode: %d]: %s",
				ht.getId(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
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
					"Error SQL al cerrar recursos en findAllBestiasFromHabitat [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
		return bestiasHabitat;
	}
	
	public void removeRelation(Bestia b, Categoria cat) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_categoria where idBestia = ? and idCategoria = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.setInt(2, cat.getIdCategoria());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al desvincular categoria %d de bestia %d [SQLState: %s, ErrorCode: %d]: %s",
				cat.getIdCategoria(), b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en removeRelation (categoria) [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public void removeRelation(Bestia b, Habitat ht) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("delete from bestia_habitat where idBestia = ? and idHabitat = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.setInt(2, ht.getId());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al desvincular habitat %d de bestia %d [SQLState: %s, ErrorCode: %d]: %s",
				ht.getId(), b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en removeRelation (habitat) [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
	
	public void approve(Bestia b) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement("update bestia set estado = 'aprobado' where idBestia = ?");
			pstmt.setInt(1, b.getIdBestia());
			pstmt.executeUpdate();
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, String.format(
				"Error SQL al aprobar bestia %d [SQLState: %s, ErrorCode: %d]: %s",
				b.getIdBestia(), ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				DbConnector.getInstancia().releaseConn();
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, String.format(
					"Error SQL al cerrar recursos en approve de bestia [SQLState: %s, ErrorCode: %d]: %s",
					ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
			}
		}
	}
}
