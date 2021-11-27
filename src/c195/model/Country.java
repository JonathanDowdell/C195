package c195.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Jonathan Dowdell
 */
public class Country {
    private long countryID;
    private String country;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    public long getCountryID() {
        return countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return countryID == country1.countryID && Objects.equals(country, country1.country) && Objects.equals(createDate, country1.createDate) && Objects.equals(createBy, country1.createBy) && Objects.equals(lastUpdate, country1.lastUpdate) && Objects.equals(lastUpdatedBy, country1.lastUpdatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryID, country, createDate, createBy, lastUpdate, lastUpdatedBy);
    }

    @Override
    public String toString() {
        return country;
    }
}
