package c195.dao;

import c195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDAO {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static User getUser(long userID) {
        String getUserQuery = "SELECT * FROM users WHERE User_ID = ?";
        User user = new User();

        try {
            PreparedStatement statement = SQLDBService.getConnection().prepareStatement(getUserQuery);
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user.setUserID(userID);
                user.setUsername(resultSet.getString("User_Name"));
                user.setPassword(resultSet.getString("Password"));
                String createDateString = resultSet.getString("Create_Date");
                LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
                user.setCreateDate(createDate);
                user.setCreatedBy(resultSet.getString("Created_By"));
                String lastUpdateString = resultSet.getString("Last_Update");
                LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
                user.setLastUpdated(lastUpdate);
                user.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));

            } else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String getAllUsersQuery = "SELECT * FROM users";
        try (Statement statement = SQLDBService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);
            while (resultSet.next()) {
                String createTimeString = resultSet.getString("Create_Time");
                String lastUpdateString = resultSet.getString("Last_Update");
                User user = new User();
                user.setUsername(resultSet.getString("User_Name"));
                user.setPassword(resultSet.getString("Password"));
                user.setCreatedBy(resultSet.getString("Last_Updated_By"));
                user.setLastUpdatedBy(resultSet.getString("Created_By"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    public static User pullUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUsername(resultSet.getString("User_Name"));
        user.setPassword(resultSet.getString("Password"));
        String createDateString = resultSet.getString("Create_Date");
        LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
        user.setCreateDate(createDate);
        user.setCreatedBy(resultSet.getString("Created_By"));
        String lastUpdateString = resultSet.getString("Last_Update");
        LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
        user.setLastUpdated(lastUpdate);
        user.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));
        return user;
    }

    // TODO: Login Attempt
    public static Boolean login(String username, String password) {

        try {
            Statement statement = SQLDBService.getConnection().createStatement();
            String loginQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
            ResultSet resultSet = statement.executeQuery(loginQuery);
            if (resultSet.next()) {
                currentUser = new User();
                currentUser.setUsername(resultSet.getString("User_Name"));
                statement.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
