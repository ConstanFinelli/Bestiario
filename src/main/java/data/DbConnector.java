package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import helpers.EnvHelper;

public class DbConnector {

    private static DbConnector instancia;
    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
    
    private String driver = EnvHelper.get("DB_DRIVER");
    private String host = EnvHelper.get("DB_HOST");
    private String port = EnvHelper.get("DB_PORT");
    private String user = EnvHelper.get("DB_USER");
    private String password = EnvHelper.get("DB_PASSWORD");
    private String db = EnvHelper.get("DB_NAME");

    private final ThreadLocal<Connection> connHolder = new ThreadLocal<>();
    private final ThreadLocal<Integer> conectadosHolder = ThreadLocal.withInitial(() -> 0);

    private DbConnector() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error crítico al instanciar el DbConnector", e);
        }
    }

    public static synchronized DbConnector getInstancia() {
        if (instancia == null) {
            instancia = new DbConnector();
        }
        return instancia;
    }

    public Connection getConn() {
        try {
            Connection conn = connHolder.get();
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, password);
                connHolder.set(conn);
                conectadosHolder.set(0);
            }
            conectadosHolder.set(conectadosHolder.get() + 1);
            return conn;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error crítico al conectar a la base de datos", e);
            throw new RuntimeException("No se pudo establecer conexión con la base de datos", e);
        }
    }

    public void releaseConn() {
        int count = conectadosHolder.get() - 1;
        conectadosHolder.set(count);
        try {
            if (count <= 0) {
                Connection conn = connHolder.get();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
                connHolder.remove();
                conectadosHolder.remove();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error crítico al liberar la conexión a la base de datos", e);
        }
    }

}
