package at.fhtw.bif3.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                System.getenv("url"),
                System.getenv("username"),
                System.getenv("password"));
    }
}
