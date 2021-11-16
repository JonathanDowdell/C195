package c195.dao;

import c195.model.Customer;
import c195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String getAllUsersQuery = "SELECT * FROM customers";
        try (Statement statement = SQLDBService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);
            while (resultSet.next()) {
                Customer customer = new Customer();
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
