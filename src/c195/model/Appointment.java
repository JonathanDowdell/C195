package c195.model;

import c195.dao.AppointmentDAO;
import c195.exception.InvalidAppointmentException;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Appointment {
    private long appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private Customer customer;
    private User user;
    private Contact contact;

    public long getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return appointmentID == that.appointmentID && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(location, that.location) && Objects.equals(type, that.type) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(createDate, that.createDate) && Objects.equals(createdBy, that.createdBy) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(lastUpdatedBy, that.lastUpdatedBy) && Objects.equals(customer, that.customer) && Objects.equals(user, that.user) && Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customer, user, contact);
    }

    public boolean validate() throws InvalidAppointmentException {

        if (title == null || title.isEmpty()) {
            throw new InvalidAppointmentException("Missing Title");
        }

        if (description == null || description.isEmpty()) {
            throw new InvalidAppointmentException("Missing Description");
        }

        if (location == null || location.isEmpty()) {
            throw new InvalidAppointmentException("Missing Location");
        }

        if (type == null || type.isEmpty()) {
            throw new InvalidAppointmentException("Missing Type");
        }

        if (customer == null) {
            throw new InvalidAppointmentException("Missing Customer");
        }

        if (contact == null) {
            throw new InvalidAppointmentException("Missing Contact");
        }

        if (start == null) {
            throw new InvalidAppointmentException("Missing Start Time");
        } else if (timeOutsideWorkHours(start)) {
            throw new InvalidAppointmentException("Start Time not within working hours");
        } else if (dayOutsideWorkHours(start)) {
            throw new InvalidAppointmentException("Start Day not within working hours");
        }

        if (end == null) {
            throw new InvalidAppointmentException("Missing End Time");
        } else if (timeOutsideWorkHours(end)) {
            throw new InvalidAppointmentException("End Time not within working hours");
        } else if (dayOutsideWorkHours(end)) {
            throw new InvalidAppointmentException("End Day not within working hours");
        }

        if (!dateTimeCorrectOrder(start, end)) {
            throw new InvalidAppointmentException("End Date is before Start Date");
        }

        final ObservableList<Appointment> overlappingAppointments = AppointmentDAO.getOverlappingAppointments(start, end);
        if (overlappingAppointments.size() > 0) {
            throw new InvalidAppointmentException("Overlapping Appointment");
        }

        return true;
    }

    private boolean dateTimeCorrectOrder(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    private boolean timeOutsideWorkHours(LocalDateTime localDateTime)  {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:m a");
        final ZonedDateTime currentZonedDateTimeEST = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));

        final LocalDateTime openDateTime = LocalDateTime
                .parse(currentZonedDateTimeEST.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 7:59 AM", dateTimeFormatter);
        final ZonedDateTime openZonedDateTimeEST = openDateTime.atZone(ZoneId.of("America/New_York"));

        final LocalDateTime closeDateTime = LocalDateTime
                .parse(currentZonedDateTimeEST.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 10:01 PM", dateTimeFormatter);
        final ZonedDateTime closeZonedDateTimeEST = closeDateTime.atZone(ZoneId.of("America/New_York"));
        return !currentZonedDateTimeEST.isAfter(openZonedDateTimeEST) || !currentZonedDateTimeEST.isBefore(closeZonedDateTimeEST);
    }

    private boolean dayOutsideWorkHours(LocalDateTime localDateTime)  {
        final ZonedDateTime currentZonedDateTimeEST = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
        return currentZonedDateTimeEST.getDayOfWeek() == DayOfWeek.SATURDAY
                || currentZonedDateTimeEST.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
