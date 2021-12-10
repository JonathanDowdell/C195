package c195.controller;

import c195.dao.AppointmentDAO;
import c195.model.Appointment;
import c195.model.Contact;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Jonathan Dowdell
 */
public class ReportViewController implements Initializable {

    @FXML
    public ComboBox<String> typeSectionMonthComboBox;

    @FXML
    public ComboBox<String> typeSectionTypeComboBox;

    @FXML
    public Label typeCountLoadingLabel;

    @FXML
    public Label contactLoadingLabel;

    @FXML
    public Label locationLoadingLabel;

    @FXML
    public TableColumn<Appointment, String> typeSectionMonthColumn;

    @FXML
    public TableColumn<Appointment, String> typeSectionTypeColumn;

    @FXML
    public TableColumn<Appointment, Long> typeSectionCountColumn;

    @FXML
    public TableView<Appointment> typesTable;

    @FXML
    public ComboBox<String> locationSectionMonthComboBox;

    @FXML
    public ComboBox<String> locationSectionComboBox;

    @FXML
    public TableColumn<Appointment, String> locationSectionMonthColumn;

    @FXML
    public TableColumn<Appointment, String> locationSectionColumn;

    @FXML
    public TableColumn<Appointment, Long> locationSectionCountColumn;

    @FXML
    public TableView<Appointment> locationsTable;

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
    public ComboBox<Contact> appointmentContactComboBox;


    private ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleTypeSectionTable();

        handleTypeSectionComboBoxes();

        handleAppointmentByContactTable();

        handleAppointmentByContactComboBox();

        handleLocationSectionTable();

        handleLocationSectionComboBoxes();
    }

    /**
     * Navigates to Preview View.
     * @param event
     */
    @FXML
    private void backButtonAction(ActionEvent event) {
        try {
            NavigationHelper.homeView(event);
        } catch (IOException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Navigating To Main View");
        }
    }

    private void handleLocationSectionTable() {
        locationSectionMonthColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getStart().getMonth().toString()));
        });

        locationSectionColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getLocation()));
        });

        locationSectionCountColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            final String location = appointment.getLocation();
            final String monthString = locationSectionMonthComboBox.getSelectionModel().getSelectedItem();
            long count;
            if (monthString != null) {
                final Month month = Month.valueOf(monthString.toUpperCase());
                count = appointments.stream()
                        .filter(a -> month.equals(a.getStart().getMonth()) && location.equalsIgnoreCase(a.getLocation()))
                        .count();
            } else {
                count = appointments.stream()
                        .filter(a -> location.equalsIgnoreCase(a.getLocation()))
                        .count();
            }

            return new ReadOnlyObjectWrapper<>(count);
        });
    }

    private void handleLocationSectionComboBoxes() {
        final ObservableList<String> months = Arrays.stream(Month.values())
                .map(uppercaseFirstLetter())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        locationSectionMonthComboBox.setItems(months);
        locationSectionMonthComboBox.getSelectionModel().clearSelection();

        final ObservableList<String> locations = appointments.stream()
                .map(appointment -> uppercaseFirstLetter(appointment.getLocation()))
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        locationSectionComboBox.setItems(locations);
    }

    /**
     * Generate Location Section By Fields.
     */
    @FXML
    public void locationSectionGenerateAction() {
        final ObservableList<Appointment> filteredMonthAppointments = appointments.stream()
                .filter(appointment -> {
                    final String month = locationSectionMonthComboBox.getSelectionModel().getSelectedItem();
                    if (month != null) {
                        return appointment.getStart().getMonth().toString().equalsIgnoreCase(month);
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        final ObservableList<Appointment> filteredTypeAppointments = filteredMonthAppointments.stream()
                .filter(appointment -> {
                    final String type = locationSectionComboBox.getSelectionModel().getSelectedItem();
                    if (type != null) {
                        return appointment.getLocation().equalsIgnoreCase(type);
                    }
                    return true;
                })
                .filter(distinctByKey(Appointment::getLocation))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (filteredTypeAppointments.isEmpty()) {
            locationLoadingLabel.setText("No Results");
        } else {
            locationLoadingLabel.setText("");
        }

        locationsTable.setItems(filteredTypeAppointments);
    }

    /**
     * Resets Fields.
     */
    @FXML
    public void locationClearAction() {
        locationLoadingLabel.setText("");
        locationsTable.setItems(FXCollections.emptyObservableList());
        locationSectionMonthComboBox.setPromptText("Month");
        locationSectionMonthComboBox.getSelectionModel().clearSelection();
        locationSectionMonthComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Month");
                } else {
                    setText(item);
                }
            }
        });

        locationSectionComboBox.setPromptText("Location");
        locationSectionComboBox.getSelectionModel().clearSelection();
        locationSectionComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Location");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void handleTypeSectionTable() {
        typeSectionMonthColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getStart().getMonth().toString()));
        });

        typeSectionTypeColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            return new ReadOnlyObjectWrapper<>(uppercaseFirstLetter(appointment.getType()));
        });

        typeSectionCountColumn.setCellValueFactory(value -> {
            final Appointment appointment = value.getValue();
            final String type = appointment.getType();
            final String monthString = typeSectionMonthComboBox.getSelectionModel().getSelectedItem();
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

    private void handleTypeSectionComboBoxes() {
        final ObservableList<String> months = Arrays.stream(Month.values())
                .map(uppercaseFirstLetter())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        typeSectionMonthComboBox.setItems(months);

        final ObservableList<String> types = appointments.stream()
                .map(Appointment::getType)
                .map(uppercaseFirstLetter())
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        typeSectionTypeComboBox.setItems(types);
    }

    /**
     * Generate Type By Fields.
     */
    @FXML
    public void typeSectionGenerateAction() {
        final ObservableList<Appointment> filteredMonthAppointments = appointments.stream()
                .filter(appointment -> {
                    final String month = typeSectionMonthComboBox.getSelectionModel().getSelectedItem();
                    if (month != null) {
                        return appointment.getStart().getMonth().toString().equalsIgnoreCase(month);
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        final ObservableList<Appointment> filteredTypeAppointments = filteredMonthAppointments.stream()
                .filter(appointment -> {
                    final String type = typeSectionTypeComboBox.getSelectionModel().getSelectedItem();
                    if (type != null) {
                        return appointment.getType().equalsIgnoreCase(type);
                    }
                    return true;
                })
                .filter(distinctByKey(Appointment::getType))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (filteredTypeAppointments.isEmpty()) {
            typeCountLoadingLabel.setText("No Results");
        } else {
            typeCountLoadingLabel.setText("");
        }

        typesTable.setItems(filteredTypeAppointments);
    }

    /**
     * Resets Type Section.
     */
    @FXML
    public void typeClearAction() {
        typesTable.setItems(FXCollections.emptyObservableList());

        typeCountLoadingLabel.setText("");
        typeSectionMonthComboBox.setPromptText("Month");
        typeSectionMonthComboBox.getSelectionModel().clearSelection();
        typeSectionMonthComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Month");
                } else {
                    setText(item);
                }
            }
        });

        typeSectionTypeComboBox.setPromptText("Type");
        typeSectionTypeComboBox.getSelectionModel().clearSelection();
        typeSectionTypeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Type");
                } else {
                    setText(item);
                }
            }
        });
    }

    private void handleAppointmentByContactTable() {
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

    private void handleAppointmentByContactComboBox() {
        ObservableList<Contact> contacts = appointments.stream().map(Appointment::getContact)
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        appointmentContactComboBox.setItems(contacts);
        appointmentContactComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                final ObservableList<Appointment> filteredAppointments = appointments.stream()
                        .filter(appointment -> appointment.getContact().getContactID() == newValue.getContactID())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                if (filteredAppointments.isEmpty()) {
                    contactLoadingLabel.setText("No Results");
                } else {
                    contactLoadingLabel.setText("");
                }
                appointmentTable.setItems(filteredAppointments);
            }
        });
    }

    /**
     * Clear Customer Appointment Section.
     */
    @FXML
    public void contactAppointmentClearAction() {
        contactLoadingLabel.setText("");
        appointmentTable.setItems(FXCollections.emptyObservableList());
        appointmentContactComboBox.setPromptText("Contact");
        appointmentContactComboBox.getSelectionModel().clearSelection();
        appointmentContactComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText("Contact");
            }
        });
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

    /**
     * Filters Object by Distinct Field (key).
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

