package c195.dao;

import c195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class UserDAO {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
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
