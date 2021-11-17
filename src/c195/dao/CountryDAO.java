package c195.dao;

import c195.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CountryDAO {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ObservableList<Country> getAllCountries() {
        String getAllCountriesQuery = "SELECT * FROM countries";
        ObservableList<Country> countries = FXCollections.observableArrayList();
        try(Statement statement = SQLDBService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAllCountriesQuery);
            while (resultSet.next()) {
                Country country = new Country();
                country.setCountryID(resultSet.getLong("Country_ID"));
                country.setCountry(resultSet.getString("Country"));

                String createDateString = resultSet.getString("Create_Date");
                LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
                country.setCreateDate(createDate);
                country.setCreateBy(resultSet.getString("Created_By"));

                String lastUpdatedString = resultSet.getString("Last_Update");
                LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdatedString, formatter);
                country.setLastUpdate(lastUpdate);
                country.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));

                countries.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countries;
    }

}
