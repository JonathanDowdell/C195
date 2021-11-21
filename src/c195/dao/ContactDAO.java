package c195.dao;

import c195.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactDAO {
    public static Contact pullContactFromResultSet(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setContactID(resultSet.getLong("Contact_ID"));
        contact.setContactName(resultSet.getString("Contact_Name"));
        contact.setEmail(resultSet.getString("Email"));
        return contact;
    }

    public static ObservableList<Contact> getAllContacts() {
        String getAllSQLQuery = "SELECT * FROM contacts";
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try(Statement statement = SQLDBService.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery(getAllSQLQuery);
            while (resultSet.next()) {
                final Contact contact = pullContactFromResultSet(resultSet);
                contacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contacts;
    }
}
