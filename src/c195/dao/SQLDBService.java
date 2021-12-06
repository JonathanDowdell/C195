package c195.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Jonathan Dowdell
 */
public class SQLDBService {
    private static Connection connection;

    public SQLDBService() {}

    /**
     * Connect to SQL Database.
     */
    public static void connect() {

        try(FileInputStream fileInputStream = new FileInputStream("./config.properties")) {
            final Properties properties = new Properties();
            properties.load(fileInputStream);

            final String protocol = properties.getProperty("database.protocol");
            final String vendor = properties.getProperty("database.vendor");
            final String location = properties.getProperty("database.location");
            final String databaseName = properties.getProperty("database.name");
            final String driver = properties.getProperty("database.driver");
            final String username = properties.getProperty("database.username");
            final String password = properties.getProperty("database.password");

            final String jdbcUrl = protocol + ":" + vendor + ":" + "//" + location + "/" + databaseName + "?connectionTimeZone = SERVER";

            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from SQL Database.
     */
    public static void disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected From MySQL Database");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
