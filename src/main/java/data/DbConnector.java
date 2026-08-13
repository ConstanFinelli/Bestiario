package data;

import java.sql.*;
import helpers.EnvHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnector {

    private static DbConnector instancia;
    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
    
    private String driver=EnvHelper.get("DB_DRIVER");
    private String host=EnvHelper.get("DB_HOST");
    private String port=EnvHelper.get("DB_PORT");
    private String user=EnvHelper.get("DB_USER");
    private String password=EnvHelper.get("DB_PASSWORD");
    private String db=EnvHelper.get("DB_NAME");
    private int conectados=0;
    private Connection conn=null;

    private DbConnector() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
        	logger.log(Level.SEVERE, "Error crítico al instanciar el DbConnector", e);
        }
    }

    public static DbConnector getInstancia() {
        if (instancia == null) {
            instancia = new DbConnector();
        }
        return instancia;
    }

    public Connection getConn() {
        try {
            if(conn==null ||conn.isClosed() ||conn.isValid(10)) {
                conn=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db, user, password);
                conectados=0;
            }
        } catch (SQLException e) {
        	logger.log(Level.SEVERE, "Error crítico al conectar a la base de datos", e);
        	throw new RuntimeException("No se pudo establecer conexión con la base de datos", e);
        }
        conectados++;
        return conn;
    }

    public void releaseConn() {
        conectados--;
        try {
            if (conectados<=0 && conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        	logger.log(Level.SEVERE, "Error crítico al liberar la conexión a la base de datos", e);
        }
    }

}
