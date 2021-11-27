package c195.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Jonathan Dowdell
 */
public class FirstLevelDivision {
    private long divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdByDate;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private Country country;

    public long getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(long divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedByDate() {
        return createdByDate;
    }

    public void setCreatedByDate(String createdByDate) {
        this.createdByDate = createdByDate;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstLevelDivision that = (FirstLevelDivision) o;
        return divisionID == that.divisionID && Objects.equals(division, that.division) && Objects.equals(createDate, that.createDate) && Objects.equals(createdByDate, that.createdByDate) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(lastUpdatedBy, that.lastUpdatedBy) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(divisionID, division, createDate, createdByDate, lastUpdate, lastUpdatedBy, country);
    }

    @Override
    public String toString() {
        return division;
    }
}
