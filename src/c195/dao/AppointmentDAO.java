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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class AppointmentDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter utcDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static boolean removeAppointment(Appointment appointment) {
        String deletionQuery = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try(PreparedStatement statement = SQLDBService.getConnection().prepareStatement(deletionQuery)) {
            statement.setLong(1, appointment.getAppointmentID());
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

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

    public static ObservableList<Appointment> getOverlappingAppointments(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        final String getAllOverlappingAppointmentsQuery = """
                SELECT * FROM appointments as a
                JOIN customers as c
                ON a.Customer_ID = c.Customer_ID
                JOIN users as u
                ON a.User_ID = u.User_ID
                JOIN contacts as cont
                ON a.Contact_ID = cont.Contact_ID
                JOIN customers as cus
                WHERE (a.Start >= ? AND a.End <= ?)
                OR (a.Start <= ? AND a.End >= ?)
                OR (a.Start BETWEEN ? AND ? OR a.End BETWEEN ? AND ?);""";

        try (PreparedStatement statement = SQLDBService.getConnection().prepareStatement(getAllOverlappingAppointmentsQuery)) {
            final String startDateTimeValue = LocalDateTime.ofInstant(start.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"))
                    .toString().replace("T", " ");
            final String endDateTimeValue = LocalDateTime.ofInstant(end.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"))
                    .toString().replace("T", " ");
            statement.setString(1, startDateTimeValue);
            statement.setString(2, endDateTimeValue);
            statement.setString(3, startDateTimeValue);
            statement.setString(4, endDateTimeValue);
            statement.setString(5, startDateTimeValue);
            statement.setString(6, endDateTimeValue);
            statement.setString(7, startDateTimeValue);
            statement.setString(8, endDateTimeValue);
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final Appointment appointment = pullAppointmentResultSet(resultSet);
                appointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }

    public static void addAppointment(Appointment appointment) {
        final String createAppointmentSQLQuery = """
                INSERT INTO appointments(
                Title,\sDescription,\sLocation,\sType,\s
                Start, End,\sCreate_Date, Created_By,\s
                Last_Update, Last_Updated_By,\s
                Customer_ID, User_ID, Contact_ID)\s
                VALUES (?,?,?,?,?,?,now(),?,now(),?,?,?,?);""";
        LocalDateTime startUTCLocalTime = LocalDateTime.ofInstant(appointment.getStart().toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        LocalDateTime endUTCLocalTime = LocalDateTime.ofInstant(appointment.getEnd().toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        try (PreparedStatement statement = SQLDBService.getConnection().prepareStatement(createAppointmentSQLQuery)) {
            final User currentUser = UserDAO.getCurrentUser();
            statement.setString(1, appointment.getTitle());
            statement.setString(2, appointment.getDescription());
            statement.setString(3, appointment.getLocation());
            statement.setString(4, appointment.getType());
            final String startUTCTime = startUTCLocalTime.toString().replace("T", " ");
            statement.setString(5, startUTCTime);
            final String endUTCTime = endUTCLocalTime.toString().replace("T", " ");
            statement.setString(6, endUTCTime);
            statement.setString(7, currentUser.getUsername());
            statement.setString(8, currentUser.getUsername());
            statement.setLong(9, appointment.getCustomer().getCustomerID());
            statement.setLong(10, currentUser.getUserID());
            statement.setLong(11, appointment.getContact().getContactID());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        String getAllAppointmentsQuery = """
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
