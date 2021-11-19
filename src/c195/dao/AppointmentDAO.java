package c195.dao;

import c195.model.Appointment;
import c195.model.Contact;
import c195.model.Customer;
import c195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String getAllAppointmentsQuery = "SELECT * FROM appointments AS a " +
                "JOIN customers as c " +
                "ON a.Customer_ID = c.Customer_ID " +
                "JOIN users as u " +
                "ON a.User_ID = u.User_ID " +
                "JOIN contacts as cont " +
                "ON a.Contact_ID = cont.Contact_ID";
        try (Statement statement = SQLDBService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllAppointmentsQuery);
            while (resultSet.next()) {
                Appointment appointment = pullAppointmentResultSet(resultSet);
                appointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointments;
    }

    private static Appointment pullAppointmentResultSet(ResultSet resultSet) throws SQLException {
        Appointment appointment = new Appointment();
        long appointmentID = resultSet.getLong("Appointment_ID");
        String title = resultSet.getString("Title");
        String description = resultSet.getString("Description");
        String location = resultSet.getString("Location");
        String type = resultSet.getString("Type");
        String startDateString = resultSet.getString("Start");
        LocalDateTime startDate = LocalDateTime.parse(startDateString, formatter);
        String endDateString = resultSet.getString("End");
        LocalDateTime endDate = LocalDateTime.parse(endDateString, formatter);
        String createDateString = resultSet.getString("Create_Date");
        LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
        String createdBy = resultSet.getString("Created_By");
        String lastUpdateString = resultSet.getString("Last_Update");
        LocalDateTime lastUpdateDate = LocalDateTime.parse(lastUpdateString, formatter);
        String lastUpdatedBy = resultSet.getString("Last_Updated_By");

        appointment.setAppointmentID(appointmentID);
        appointment.setTitle(title);
        appointment.setDescription(description);
        appointment.setLocation(location);
        appointment.setType(type);
        appointment.setStart(startDate);
        appointment.setEnd(endDate);
        appointment.setCreateDate(createDate);
        appointment.setCreatedBy(createdBy);
        appointment.setLastUpdate(lastUpdateDate);
        appointment.setLastUpdatedBy(lastUpdatedBy);

        Customer customer = CustomerDAO.pullCustomerFromResultSet(resultSet);
        appointment.setCustomer(customer);


        User user = UserDAO.pullUserFromResultSet(resultSet);
        appointment.setUser(user);


        Contact contact = ContactDAO.pullContactFromResultSet(resultSet);
        appointment.setContact(contact);
        return appointment;
    }

    public static ObservableList<Appointment> getAppointmentByCustomer(Customer customer) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String getAllAppointmentsQuery =
        """
                SELECT * FROM appointments AS a\040
                                JOIN customers as c\040
                                ON a.Customer_ID = c.Customer_ID\040
                                JOIN users as u\040
                                ON a.User_ID = u.User_ID\040
                                JOIN contacts as cont\040
                                ON a.Contact_ID = cont.Contact_ID\040
                                WHERE a.Customer_ID = ?
                """;

        try {
            PreparedStatement statement = SQLDBService.getConnection().prepareStatement(getAllAppointmentsQuery);
            statement.setLong(1,customer.getCustomerID());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = pullAppointmentResultSet(resultSet);
                appointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }
}
