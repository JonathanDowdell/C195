package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.CustomerDAO;
import c195.model.Appointment;
import c195.model.Customer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportViewController implements Initializable {

    @FXML
    public ComboBox<String> monthComboBox;

    @FXML
    public ComboBox<String> typeComboBox;

    @FXML
    public TableColumn<Appointment, String> monthColumn;

    @FXML
    public TableColumn<Appointment, String> typeColumn;

    @FXML
    public TableColumn<Appointment, Long> countColumn;

    @FXML
    public TableView<Appointment> typesTable;

    @FXML
    public TableView<Appointment> appointmentTable;

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
    public ComboBox<Customer> appointmentCustomerComboBox;

    private final ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();

    private final ObservableList<Customer> customer = CustomerDAO.getAllCustomers();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleCustomerAppointmentTable();

        handleCustomerAppointmentComboBox();

        handleTypeSectionTable();

        handleTypeSectionComboBoxes();
    }

    private void handleCustomerAppointmentTable() {
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
    }

    private void handleCustomerAppointmentComboBox() {
        appointmentCustomerComboBox.setItems(customer);
        appointmentCustomerComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                final ObservableList<Appointment> filteredAppointments = appointments.stream()
                        .filter(appointment -> appointment.getCustomer().getCustomerID() == newValue.getCustomerID())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                appointmentTable.setItems(filteredAppointments);
            }
        });
    }

    private void handleTypeSectionTable() {
        monthColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getStart().getMonth().toString()));
        });

        typeColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getType()));
        });

        countColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            final String type = appointment.getType();
            final String monthString = monthComboBox.getSelectionModel().getSelectedItem();
            long count;
            if (monthString != null) {
                final Month month = Month.valueOf(monthString.toUpperCase());
                count = appointments.stream()
                        .filter(a -> month.equals(a.getStart().getMonth()) && type.equalsIgnoreCase(a.getType()))
                        .count();
            } else {
                count = appointments.stream()
                        .filter(a -> type.equalsIgnoreCase(a.getType()))
                        .count();
            }

            return new ReadOnlyObjectWrapper<>(count);
        });
    }

    @FXML
    public void typeSectionGenerateAction(ActionEvent event) {
        final ObservableList<Appointment> filteredMonthAppointments = appointments.stream()
                .filter(appointment -> {
                    final String month = monthComboBox.getSelectionModel().getSelectedItem();
                    if (month != null) {
                        return appointment.getStart().getMonth().toString().equalsIgnoreCase(month);
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        final ObservableList<Appointment> filteredTypeAppointments = filteredMonthAppointments.stream()
                .filter(appointment -> {
                    final String type = typeComboBox.getSelectionModel().getSelectedItem();
                    if (type != null) {
                        return appointment.getType().equalsIgnoreCase(type);
                    }
                    return true;
                })
                .filter(distinctByKey(Appointment::getType))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        typesTable.setItems(filteredTypeAppointments);
    }

    @FXML
    public void typeResetAction(ActionEvent event) {
        typesTable.setItems(FXCollections.emptyObservableList());
        typeComboBox.setPromptText("Type");
        monthComboBox.setPromptText("Month");
        handleTypeSectionComboBoxes();
    }

    @FXML
    public void customerAppointmentResetAction(ActionEvent event) {
        appointmentTable.setItems(appointments);
        appointmentCustomerComboBox.setPromptText("Customer");
        appointmentCustomerComboBox.getSelectionModel().clearSelection();
    }

    private void handleTypeSectionComboBoxes() {
        final ObservableList<String> months = Arrays.stream(Month.values())
                .map(uppercaseFirstLetter())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        monthComboBox.setItems(months);
        monthComboBox.getSelectionModel().clearSelection();

        final ObservableList<String> types = appointments.stream()
                .map(Appointment::getType)
                .map(uppercaseFirstLetter())
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        typeComboBox.setItems(types);
        typeComboBox.getSelectionModel().clearSelection();
    }

    private <T> Function<T, String> uppercaseFirstLetter() {
        return T -> {
            final String monthString = T.toString();
            return monthString.substring(0, 1).toUpperCase() + monthString.toLowerCase().substring(1);
        };
    }

    private String uppercaseFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.toLowerCase().substring(1);
    }

    public <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

