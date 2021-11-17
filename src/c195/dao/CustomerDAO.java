package c195.dao;

import c195.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Customer getCustomer(long customerID) {
        String getCustomerQuery = "SELECT * FROM customers WHERE Customer_ID = ?";
        Customer fetchedCustomer = new Customer();

        try {
            PreparedStatement statement = SQLDBService.getConnection().prepareStatement(getCustomerQuery);
            statement.setLong(1, customerID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fetchedCustomer.setCustomerID(customerID);
                fetchedCustomer.setCustomerName(resultSet.getString("Customer_Name"));
                fetchedCustomer.setAddress(resultSet.getString("Address"));
                fetchedCustomer.setPostalCode(resultSet.getString("Postal_Code"));
                fetchedCustomer.setPhone(resultSet.getString("Phone"));
                String createDateString = resultSet.getString("Create_Date");
                LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
                fetchedCustomer.setCreateDate(createDate);
                fetchedCustomer.setCreatedBy(resultSet.getString("Created_By"));
                String lastUpdateString = resultSet.getString("Last_Update");
                LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
                fetchedCustomer.setLastUpdate(lastUpdate);
                fetchedCustomer.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));
            } else {
                return null;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return fetchedCustomer;
    }

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String getAllUsersQuery = "SELECT * FROM customers";
        try (Statement statement = SQLDBService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);
            while (resultSet.next()) {
                Customer customer = new Customer();
                long customerID = resultSet.getLong("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                String createDateString = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
                String lastUpdateString = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                LocalDateTime lateUpdate = LocalDateTime.parse(lastUpdateString, formatter);

                customer.setCustomerID(customerID);
                customer.setCustomerName(name);
                customer.setAddress(address);
                customer.setPostalCode(postalCode);
                customer.setPhone(phone);
                customer.setCreateDate(createDate);
                customer.setCreatedBy(createdBy);
                customer.setLastUpdate(lateUpdate);
                customer.setCreatedBy(lastUpdatedBy);

                customers.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customers;
    }
}
