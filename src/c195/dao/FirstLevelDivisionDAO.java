package c195.dao;

import c195.model.Country;
import c195.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jonathan Dowdell
 */
public class FirstLevelDivisionDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Load Observable List Of FirstLevelDivision.
     * @param countryID long
     * @return Get Observable List Of FirstLevelDivision.
     */
    public static ObservableList<FirstLevelDivision> getByCountryID(long countryID) {
        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();
        String firstLevelQuery = "SELECT * FROM first_level_divisions as fld JOIN countries as co ON fld.Country_ID = co.Country_ID WHERE fld.Country_ID = ?";
        try {
            PreparedStatement statement = SQLDBService.getConnection().prepareStatement(firstLevelQuery);
            statement.setLong(1, countryID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision();
                firstLevelDivision.setDivisionID(resultSet.getLong("Division_ID"));
                firstLevelDivision.setDivision(resultSet.getString("Division"));

                String createDateString = resultSet.getString("Create_Date");
                LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
                firstLevelDivision.setCreateDate(createDate);
                firstLevelDivision.setCreatedByDate(resultSet.getString("Created_By"));

                String lastUpdateString = resultSet.getString("Last_Update");
                LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
                firstLevelDivision.setLastUpdate(lastUpdate);
                firstLevelDivision.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));



                firstLevelDivisions.add(firstLevelDivision);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return firstLevelDivisions;
    }

    public static FirstLevelDivision pullDivisionFromResultSet(ResultSet resultSet) throws SQLException {
        FirstLevelDivision firstLevelDivision = new FirstLevelDivision();
        firstLevelDivision.setDivisionID(resultSet.getLong("Division_ID"));
        firstLevelDivision.setDivision(resultSet.getString("Division"));

        String createDateString = resultSet.getString("Create_Date");
        LocalDateTime createDate = LocalDateTime.parse(createDateString, formatter);
        firstLevelDivision.setCreateDate(createDate);
        firstLevelDivision.setCreatedByDate(resultSet.getString("Created_By"));

        String lastUpdateString = resultSet.getString("Last_Update");
        LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
        firstLevelDivision.setLastUpdate(lastUpdate);
        firstLevelDivision.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));

        Country country = CountryDAO.pullCustomerFromResultSet(resultSet);
        firstLevelDivision.setCountry(country);

        return firstLevelDivision;
    }
}
