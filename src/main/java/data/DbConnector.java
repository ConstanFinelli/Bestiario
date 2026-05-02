package data;

import java.sql.*;
import helpers.EnvHelper;

public class DbConnector {

    private static DbConnector instancia;

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
            e.printStackTrace();
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
            if(conn==null || conn.isClosed()) {
                conn=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db, user, password);
                conectados=0;
            }
        } catch (SQLException e) {
        	System.out.println("jdbc:mysql://"+host+":"+port+"/"+db + user + password);
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

}
