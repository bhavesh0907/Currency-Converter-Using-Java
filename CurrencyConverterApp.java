import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyConverterApp {
    private JFrame frame;
    private JPanel panel;
    private JLabel fromLabel, toLabel, resultLabel;
    private JComboBox<String> fromCurrencyComboBox, toCurrencyComboBox;
    private JTextField amountTextField;
    private JButton convertButton;

    public CurrencyConverterApp() {
        frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        fromLabel = new JLabel("From Currency:");
        toLabel = new JLabel("To Currency:");
        resultLabel = new JLabel("Result: ");

        fromCurrencyComboBox = new JComboBox<>();
        toCurrencyComboBox = new JComboBox<>();
        amountTextField = new JTextField();
        convertButton = new JButton("Convert");

        // Add currencies to the ComboBoxes (these can be fetched from the database)
        String[] currencies = {"USD", "EUR", "INR", "GBP", "JPY"};
        for (String currency : currencies) {
            fromCurrencyComboBox.addItem(currency);
            toCurrencyComboBox.addItem(currency);
        }

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        panel.add(fromLabel);
        panel.add(fromCurrencyComboBox);
        panel.add(toLabel);
        panel.add(toCurrencyComboBox);
        panel.add(new JLabel("Amount:"));
        panel.add(amountTextField);
        panel.add(convertButton);
        panel.add(resultLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void convertCurrency() {
        try {
            String fromCurrency = fromCurrencyComboBox.getSelectedItem().toString();
            String toCurrency = toCurrencyComboBox.getSelectedItem().toString();
            double amount = Double.parseDouble(amountTextField.getText());

            // Fetch exchange rate from the database
            double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
            if (exchangeRate > 0) {
                double convertedAmount = amount * exchangeRate;
                resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency));
            } else {
                resultLabel.setText("Exchange rate not available.");
            }
        } catch (Exception e) {
            resultLabel.setText("Invalid input.");
        }
    }

    private double getExchangeRate(String fromCurrency, String toCurrency) {
        String url = "jdbc:mysql://localhost:3306/currency_converter";
        String user = "aryan";  // Change as per your database credentials
        String password = "root@123";  // Change as per your database credentials

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT rate FROM exchange_rates WHERE currency_from = ? AND currency_to = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, fromCurrency);
                statement.setString(2, toCurrency);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getDouble("rate");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if exchange rate is not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterApp::new);
    }
}
