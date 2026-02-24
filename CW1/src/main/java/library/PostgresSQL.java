package library;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresSQL {
    
    private static HikariDataSource dataSource;

    private static volatile PostgresSQL instance;

    private static final Logger LOGGER = Logger.getLogger(PostgresSQL.class.getName());

    
    protected PostgresSQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/Library");
        config.setUsername("postgres");
        config.setPassword("google24");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }
    

    public static PostgresSQL getInstance() {
        if (instance == null) {
            synchronized (PostgresSQL.class) {
                if (instance == null) {
                    instance = new PostgresSQL();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            try {
                dataSource.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error closing the data source: {0}", e.getMessage());
            }
        }
    }
}

