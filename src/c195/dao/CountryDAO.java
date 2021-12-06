package c195.dao;

import c195.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jonathan Dowdell
 */
public class CountryDAO {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Load Customers by Country ID.
     * @param countryID long
     * @return Country
     */
    public static Country getCustomerByID(long countryID) {
        String countryFetchQuery = "SELECT * from countries " +
                "WHERE CountryID = ?";
        Country country = new Country();

        try {
            PreparedStatement statement = SQLDBService.getConnection().prepareStatement(countryFetchQuery);
            statement.setLong(1, countryID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return country;
    }

    /**
     * Get All Countries.
     * @return Observable List of Countries.
     */
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

    public static Country pullCustomerFromResultSet(ResultSet resultSet) throws SQLException {
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
        return country;
    }
}
