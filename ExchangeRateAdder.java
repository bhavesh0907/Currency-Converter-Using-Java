import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ExchangeRateAdder {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/currency_converter";
        String user = "aryan";  // Change as per your database credentials
        String password = "root@123";  // Change as per your database credentials

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter the source currency (e.g., USD): ");
            String fromCurrency = scanner.nextLine();

            System.out.print("Enter the target currency (e.g., EUR): ");
            String toCurrency = scanner.nextLine();

            System.out.print("Enter the exchange rate: ");
            double rate = scanner.nextDouble();

            // SQL query to insert the exchange rate into the database
            String insertQuery = "INSERT INTO exchange_rates (currency_from, currency_to, rate) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, fromCurrency);
                preparedStatement.setString(2, toCurrency);
                preparedStatement.setDouble(3, rate);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Exchange rate added successfully.");
                } else {
                    System.out.println("Failed to add exchange rate.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
