import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCurrencyConverterDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/";
        String user = "aryan";  // Change as per your database credentials
        String password = "root@123";  // Change as per your database credentials
        String databaseName = "currency_converter";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the MySQL server");

            // Create the database if it does not exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createDatabaseQuery);
                System.out.println("Database '" + databaseName + "' created successfully");
            }

            // Switch to the newly created database
            connection.setCatalog(databaseName);

            // Create the exchange_rates table
            String createTableQuery = "CREATE TABLE IF NOT EXISTS exchange_rates ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "currency_from VARCHAR(3) NOT NULL, "
                    + "currency_to VARCHAR(3) NOT NULL, "
                    + "rate DOUBLE NOT NULL)";
            
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableQuery);
                System.out.println("Table 'exchange_rates' created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
