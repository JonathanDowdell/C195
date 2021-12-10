package c195.controller;

import c195.dao.AppointmentDAO;
import c195.model.Appointment;
import c195.util.LocalDateTimeHelper;
import c195.util.ModalHelper;
import c195.util.NavigationHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static c195.util.ModalHelper.displayAlert;

/**
 * @author Jonathan Dowdell
 */
public class ManageAppointmentViewController implements Initializable {


    @FXML
    public TableColumn<Appointment, Long> appointmentIDColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentDescColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentLocationColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentContactColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appointmentStartColumn;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appointmentEndColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentCustomerIDColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentUserIDColumn;


    @FXML
    public Button addAppointmentButton;

    @FXML
    public Button modifyAppointmentButton;

    @FXML
    public Button deleteAppointmentButton;

    @FXML
    public TableView<Appointment> appointmentTable;

    @FXML
    public Label upcomingAppointmentsLabel;

    @FXML
    public ToggleGroup sortByGroup;

    @FXML
    public RadioButton allSortSelection;

    @FXML
    public RadioButton monthSortSelection;

    @FXML
    public RadioButton weekSortSelection;

    private final ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleAppointmentTable();
        checkAppointments();
    }

    /**
     * Check if Appointment Exist within 15 minutes.
     */
    private void checkAppointments() {
        for (final Appointment currentAppointment : appointments) {
            final LocalDateTime start = currentAppointment.getStart();
            final LocalDateTime nowDateTimeUTC = LocalDateTime.ofInstant(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            final LocalDateTime startDateTimeUTC = LocalDateTime.ofInstant(start.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            final long minutesApart = ChronoUnit.MINUTES.between(nowDateTimeUTC, startDateTimeUTC);
            if (minutesApart <= 15 && minutesApart >= 0) {
                final String content = start.format(DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a"));
                String header = "Appointment " + currentAppointment.getAppointmentID() +
                        " is within 15 minutes. " +
                        content;
                upcomingAppointmentsLabel.setText(header);
                return;
            }
        }
    }

    /**
     * Open Manage Appointment View.
     * @param event
     */
    @FXML
    public void addAppointmentAction(ActionEvent event) {
        try {
            NavigationHelper.appointmentView(event, null);
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Opening Appointment View.", "Please See Developer");
        }
    }

    /**
     * Open Manage Appointment View with selected Appointment.
     * @param event
     */
    @FXML
    public void modifyAppointmentAction(ActionEvent event) {
        try {
            final Appointment appointment = Optional.ofNullable(appointmentTable.getSelectionModel().getSelectedItem())
                    .orElseThrow();
            NavigationHelper.appointmentView(event, appointment);
        } catch (NoSuchElementException noSuchElementException) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Please Select Appointment.");
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Opening Appointment View.", "Please See Developer");
        }
    }

    /**
     * Delete Appointment using selected Appointment.
     * @param event
     */
    @FXML
    public void deleteAppointmentAction(ActionEvent event) {
        try {
            Appointment appointment = Optional.of(appointmentTable.getSelectionModel().getSelectedItem())
                    .orElseThrow();

            Alert alert = displayAlert(Alert.AlertType.CONFIRMATION,
                    "Delete Appointment",
                    "Are you sure you want to delete this appointment?",
                    "Press OK to delete the appointment. \nPress Cancel to cancel the deletion.");

            if (alert.getResult() == ButtonType.OK) {
                final boolean appointmentRemoved = AppointmentDAO.removeAppointment(appointment);
                if (appointmentRemoved) {
                    appointments.remove(appointment);
                    if (monthSortSelection.isSelected()) {
                        appointmentTable.setItems(sortAppointmentsByThisMonth());
                    } else if (weekSortSelection.isSelected()) {
                        appointmentTable.setItems(sortAppointmentsByThisWeek());
                    }
                } else {
                    displayAlert(Alert.AlertType.ERROR,
                            "Appointment Deletion Error",
                            "The appointment was NOT deleted.",
                            "Please try again.");
                }
            }

        } catch (Exception e) {
            displayAlert(Alert.AlertType.ERROR,
                    "Appointment Deletion Error",
                    "The appointment was NOT selected.",
                    "Please try select appointment.");
        }
    }

    @FXML
    public void backAction(ActionEvent event) {
        try {
            NavigationHelper.homeView(event);
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Opening Appointment View.", "Please See Developer");
        }
    }

    private void handleAppointmentTable() {
        appointments.sort(Comparator.comparing(Appointment::getAppointmentID));
        appointmentTable.setItems(appointments);
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));


        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentStartColumn.setCellFactory(LocalDateTimeHelper::dateTimeCell);

        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentEndColumn.setCellFactory(LocalDateTimeHelper::dateTimeCell);

        appointmentCustomerIDColumn.setCellValueFactory(value -> {
            long customerID = value.getValue().getCustomer().getCustomerID();
            return new ReadOnlyObjectWrapper<>(customerID);
        });

        appointmentUserIDColumn.setCellValueFactory(value -> {
            long userID = value.getValue().getUser().getUserID();
            return new ReadOnlyObjectWrapper<>(userID);
        });

        appointmentContactColumn.setCellValueFactory(value -> {
            long contactID = value.getValue().getContact().getContactID();
            return new ReadOnlyObjectWrapper<>(contactID);
        });

        sortByGroup.selectedToggleProperty().addListener((observableValue) -> {
            if (allSortSelection.isSelected()) {
                appointmentTable.setItems(appointments);
            } else if (monthSortSelection.isSelected()) {
                appointmentTable.setItems(sortAppointmentsByThisMonth());
            } else if (weekSortSelection.isSelected()) {
                appointmentTable.setItems(sortAppointmentsByThisWeek());
            }
        });
    }

    /**
     * Sorts Appointments using Lambda Expressing.
     * Lambda - ...
     * Reasoning - Decreased code footprint and Increased code readability.
     * @return Observable List of Appointments
     */
    public ObservableList<Appointment> sortAppointmentsByThisMonth() {
        final LocalDateTime now = LocalDateTime.now();
        return this.appointments.stream().filter(appointment -> {
            final LocalDateTime start = appointment.getStart();
            return start.getMonth() == now.getMonth();
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Sorts Appointments using Lambda Expressing.
     * Reasoning - Decreased code footprint and Increased code readability.
     * @return Observable List of Appointments
     */
    private ObservableList<Appointment> sortAppointmentsByThisWeek() {
        final LocalDate now = LocalDate.now();
        final TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        final int nowWeekOfYear = now.get(woy);
        return this.appointments.stream().filter(appointment -> {
            final LocalDateTime start = appointment.getStart();
            final LocalDate startLocal = LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
            final int startWeekOrYear = startLocal.get(woy);
            return nowWeekOfYear == startWeekOrYear;
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
