package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private TableView<Booking> bookingTableView;

    @FXML
    private TableColumn<Booking, Integer> idColumn;


    @FXML
    private TableColumn<Booking, String> userColumn;

    @FXML
    private TableColumn<Booking, String> showtimeColumn;

    @FXML
    private TableColumn<Booking, BigDecimal> totalPriceColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private TableColumn<Booking, Timestamp> createdAtColumn;

    @FXML
    private TextField idField;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private ComboBox<Showtime> showtimeComboBox;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button reportButton;

    @FXML
    private Label totalBookingsLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label avgBookingPriceLabel;

    @FXML
    private PieChart statusPieChart;

    @FXML
    private BarChart<String, Number> revenueByStatusChart;

    private BookingDao bookingDao;
    private UserDao userDao;
    private ShowTimeDao showtimeDao;
    private ObservableList<Booking> bookingList;
    private final String[] STATUSES = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookingDao = new BookingDao();
        userDao = new UserDao();
        showtimeDao = new ShowTimeDao();

        //khoi tao cot trong bang
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getUser().getUsername(),
                        cellData.getValue().userProperty()));
        showtimeColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> {
                            Showtime showtime = cellData.getValue().getShowtime();
                            if (showtime == null) {
                                System.out.println("Showtime is NULL for booking ID: " + cellData.getValue().getId());
                            } else {
                                System.out.println("Showtime found: " + showtime.getDisplayName());
                            }
                            return showtime != null ? showtime.getDisplayName() : "N/A";
                        },
                        cellData.getValue().showtimeProperty()));

        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        statusComboBox.getItems().addAll(STATUSES);//khoi tao combo box
        loadUsers();
        loadShowtimes();
        loadBookingData();//load danh sach book cho cac bang
        updateStatistics();//update tom tat cac thong ke

        //cai dat bang theo su kien
        bookingTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getId()));
                userComboBox.setValue(newSelection.getUser());
                showtimeComboBox.setValue(newSelection.getShowtime());
                priceField.setText(newSelection.getTotalPrice().toString());
                statusComboBox.setValue(newSelection.getStatus());

                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                clearForm();
            }
        });
    }

    private void loadUsers() {
        List<User> users = userDao.findAll();
        userComboBox.setItems(FXCollections.observableArrayList(users));
        // Set a cell factory to display username in ComboBox
        userComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });
        userComboBox.setButtonCell(new javafx.scene.control.ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });
    }

    private void loadShowtimes() {
        List<Showtime> showtimes = showtimeDao.findAll();
        showtimeComboBox.setItems(FXCollections.observableArrayList(showtimes));
        // Set a cell factory to display showtime details in ComboBox
        showtimeComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<Showtime>() {
            @Override
            protected void updateItem(Showtime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
        showtimeComboBox.setButtonCell(new javafx.scene.control.ListCell<Showtime>() {
            @Override
            protected void updateItem(Showtime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
    }

    private void loadBookingData() {
        List<Booking> bookings = bookingDao.findAll();
        bookingList = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingList);
    }

    private void updateStatistics() {
        int totalBookings = bookingList.size();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Booking booking : bookingList) {
            totalRevenue = totalRevenue.add(booking.getTotalPrice());
        }
        totalBookingsLabel.setText(String.valueOf(totalBookings));
//        totalRevenueLabel.setText("$" + totalRevenue.toPlainString());
        totalRevenueLabel.setText("$" + String.format("%.2f", totalRevenue));
    }

    @FXML
    private void handleCreateAction(ActionEvent event) {
        if (validateInput()) {
            try {
                User user = userComboBox.getValue();
                Showtime showtime = showtimeComboBox.getValue();
                BigDecimal totalPrice = new BigDecimal(priceField.getText());
                String status = statusComboBox.getValue();

                Booking booking = new Booking();
                booking.setUser(user);
                booking.setShowtime(showtime);
                booking.setTotalPrice(totalPrice);
                booking.setStatus(status);

                boolean success = bookingDao.insert(booking);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Booking added successfully!");
                    loadBookingData();
                    updateStatistics();
                    clearForm();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add booking!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleUpdateAction(ActionEvent actionEvent) {
        if (validateInput()) {
            try {
                int id = Integer.parseInt(idField.getText());
                User user = userComboBox.getValue();
                Showtime showtime = showtimeComboBox.getValue();
                BigDecimal totalPrice = new BigDecimal(priceField.getText());
                String status = statusComboBox.getValue();

                Booking booking = bookingDao.findById(id);
                if (booking != null) {
                    booking.setUser(user);
                    booking.setShowtime(showtime);
                    booking.setTotalPrice(totalPrice);
                    booking.setStatus(status);

                    bookingDao.update(booking);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Booking updated successfully!");
                    loadBookingData();
                    updateStatistics();
                    clearForm();
                }else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Booking not found!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleDeleteAction(ActionEvent actionEvent) {
        try {
            if (idField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking to delete!");
                return;
            }
            int id = Integer.parseInt(idField.getText());
            Booking booking = bookingDao.findById(id);
            if (booking != null) {
                bookingDao.delete(booking.getId().longValue());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully!");
                loadBookingData();
                updateStatistics();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Booking not found!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void handleReportAction(ActionEvent actionEvent) {
        try {
            //load view cua report
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Booking-Report-View.fxml"));
            AnchorPane reportPane = loader.load();

            //thiet lap data cho report
            BookingReportController reportController = loader.getController();
            reportController.setBookings(bookingList);

            //tao mot stage co report
            Stage reportStage = new Stage();
            reportStage.setTitle("Booking Report");
            reportStage.initModality(Modality.WINDOW_MODAL);
            reportStage.initOwner(reportButton.getScene().getWindow());

            Scene scene = new Scene(reportPane);
            reportStage.setScene(scene);
            reportStage.show();;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        idField.clear();
        userComboBox.setValue(null);
        showtimeComboBox.setValue(null);
        priceField.clear();
        statusComboBox.setValue(null);

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        bookingTableView.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        if (userComboBox.getValue() == null) {
            errors.append("Please select a user.\n");
        }
        if (showtimeComboBox.getValue() == null) {
            errors.append("Please select a showtime.\n");
        }
        if (priceField.getText().isEmpty()) {
            errors.append("Please enter a price.\n");
        } else {
            try {
                new BigDecimal(priceField.getText());
            } catch (NumberFormatException e) {
                errors.append("Price must be a valid number.\n");
            }
        }

        if (statusComboBox.getValue() == null || statusComboBox.getValue().isEmpty()) {
            errors.append("Please select a status.\n");
        }


        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", errors.toString());
            return false;
        }

        return true;
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
