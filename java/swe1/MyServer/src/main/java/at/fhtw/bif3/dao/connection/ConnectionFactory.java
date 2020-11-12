package at.fhtw.bif3.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                getProperties().getProperty("db.url"),
                getProperties().getProperty("db.username"),
                getProperties().getProperty("db.password"));
    }
}
