package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.CustomerDAO;
import c195.model.Appointment;
import c195.model.Customer;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static c195.util.ModalHelper.displayAlert;

public class MainViewController implements Initializable {

    @FXML
    public TableColumn<Customer, Long> customerIDColumn;

    @FXML
    public TableColumn<Customer, String> customerNameColumn;

    @FXML
    public TableColumn<Customer, Long> customerDivisionIDColumn;

    @FXML
    public Button addCustomerButton;

    @FXML
    public Button modifyCustomerButton;

    @FXML
    public Button deleteCustomerButton;

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
    public TableView<Customer> customerTable;

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

    private final ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    private final ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleCustomerTable();
        handleAppointmentTable();
        checkAppointments();
    }

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

    @FXML
    private void addCustomerAction(ActionEvent actionEvent) {
        try {
            NavigationHelper.customerView(actionEvent, null);
        } catch (IOException e) {
            // TODO: 11/17/2021 Handle Error
            e.printStackTrace();
        }
    }

    @FXML
    private void updateCustomerAction(ActionEvent actionEvent) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            displayAlert(Alert.AlertType.INFORMATION, "Customer not selected.", "Please select customer.");
        } else {
            try {
                NavigationHelper.customerView(actionEvent, selectedCustomer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteCustomerAction() {
        try {
            Customer customer = Optional.of(customerTable.getSelectionModel().getSelectedItem())
                    .orElseThrow();

            if (!AppointmentDAO.getAppointmentByCustomer(customer).isEmpty()) {
                displayAlert(Alert.AlertType.ERROR, "Deletion Error",
                        "Customer still has Appointment(s)",
                        "Please delete Appointment(s) first.");
                return;
            }

            Alert alert = displayAlert(Alert.AlertType.CONFIRMATION,
                    "Delete Product",
                    "Are you sure you want to delete this product?",
                    "Press OK to delete the product. \nPress Cancel to cancel the deletion.");

            if (alert.getResult() == ButtonType.OK) {
                boolean customerRemoved = CustomerDAO.removeCustomer(customer);
                if (customerRemoved) {
                    customers.remove(customer);
                } else {
                    displayAlert(Alert.AlertType.ERROR,
                            "Product Deletion Error",
                            "The product was NOT deleted.",
                            "Please try again.");
                }
            }

        } catch (Exception e) {
            displayAlert(Alert.AlertType.ERROR,
                    "Product Deletion Error",
                    "The product was NOT deleted.",
                    "Please select a product to delete.");
        }
    }

    @FXML
    public void addAppointmentAction(ActionEvent event) {
        try {
            NavigationHelper.manageAppointmentView(event, null);
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Opening Appointment View.", "Please See Developer");
        }
    }

    @FXML
    public void modifyAppointmentAction(ActionEvent event) {
        try {
            final Appointment appointment = Optional.ofNullable(appointmentTable.getSelectionModel().getSelectedItem())
                    .orElseThrow();
            NavigationHelper.manageAppointmentView(event, appointment);
        } catch (NoSuchElementException noSuchElementException) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Please Select Appointment.");
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Opening Appointment View.", "Please See Developer");
        }
    }

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
    public void navigateToReport(ActionEvent event) {
        try {
            NavigationHelper.reportView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAppointmentTable() {
        appointmentTable.setItems(appointments);
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

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

    private void handleCustomerTable() {
        customerTable.setItems(customers);
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerDivisionIDColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getDivision().getDivisionID()));
    }

    private ObservableList<Appointment> sortAppointmentsByThisMonth() {
        final LocalDateTime now = LocalDateTime.now();
        return this.appointments.stream().filter(appointment -> {
            final LocalDateTime start = appointment.getStart();
            return start.getMonth() == now.getMonth();
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

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
