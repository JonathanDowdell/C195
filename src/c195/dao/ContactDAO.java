package c195.dao;

import c195.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {
    public static Contact pullContactFromResultSet(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setContactID(resultSet.getLong("Contact_ID"));
        contact.setContactName(resultSet.getString("Contact_Name"));
        contact.setEmail(resultSet.getString("Email"));
        return contact;
    }
}
