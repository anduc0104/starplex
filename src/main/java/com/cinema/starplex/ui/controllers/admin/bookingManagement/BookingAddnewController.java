package com.cinema.starplex.ui.controllers.admin.bookingManagement;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.PaymentDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Payment;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.models.User;
import com.cinema.starplex.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class BookingAddnewController {
    @FXML
    private Button backButton, saveButton, clearButton;
    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<Showtime> showtimeComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TableColumn<Booking, Timestamp> createdAtColumn;
    @FXML
    private TextField priceField;

    private final BookingDao bookingDao = new BookingDao();
    private final UserDao userDao = new UserDao();
    private final ShowTimeDao showtimeDao = new ShowTimeDao();
    private final PaymentDao paymentDao = new PaymentDao();

    public void initialize() {
        // Gọi một lần duy nhất để tránh truy vấn thừa
        loadUsers();
        loadShowtimes();
        loadStatusOptions();
    }

    private void loadUsers() {
        List<User> users = userDao.findAll();
        userComboBox.setItems(FXCollections.observableArrayList(users));

        userComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "ID: " + item.getId() + " | " + item.getUsername() + " | " + item.getEmail());
            }
        });

        userComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "ID: " + item.getId() + " | " + item.getUsername() + " | " + item.getEmail());
            }
        });

        userComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getId() + "-" + user.getUsername() + " - " + user.getEmail() : "";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
    }

    private void loadShowtimes() {
        ObservableList<Showtime> showtimeList = FXCollections.observableArrayList(showtimeDao.findAll());
        showtimeComboBox.setItems(showtimeList);

        showtimeComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Showtime item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getDisplayName());
            }
        });

        showtimeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Showtime item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getDisplayName());
            }
        });

        showtimeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Showtime showtime) {
                return (showtime != null) ? showtime.getDisplayName() : "";
            }

            @Override
            public Showtime fromString(String string) {
                return null;
            }
        });
    }


    @FXML
    private void handleCreateAction(ActionEvent event) {
        if (!validateInput()) return;

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
            if (!success) {
                AlertUtils.showError("Error", "Failed to add booking!");
                return;
            }

            // Tạo payment ngay sau khi booking thành công
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(totalPrice);
            payment.setPaymentMethod("Credit Card");
            payment.setStatus("Pending"); // Chờ xác nhận thanh toán

            boolean paymentSuccess = paymentDao.insert(payment);
            if (!paymentSuccess) {
                AlertUtils.showWarning("Warning", "Booking created, but payment was not added!");
            }

            AlertUtils.showInfo("Success", "Booking and Payment added successfully!");
            returnToBookingView(event);
        } catch (Exception e) {
            AlertUtils.showError("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clearForm() {
        priceField.clear();
        userComboBox.getSelectionModel().clearSelection();
        showtimeComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (userComboBox.getValue() == null) errors.append("Please select a user.\n");
        if (showtimeComboBox.getValue() == null) errors.append("Please select a showtime.\n");
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
            AlertUtils.showWarning("Validation Error", errors.toString());
            return false;
        }

        return true;
    }

    private void loadStatusOptions() {
        List<String> statuses = List.of("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED");
        statusComboBox.setItems(FXCollections.observableArrayList(statuses));
    }

    @FXML
    private void handleBack(ActionEvent event) {
        returnToBookingView(event);
    }

    private void returnToBookingView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/booking-view/Booking-View.fxml"));
            Parent seatView = loader.load();

            AnchorPane root = (AnchorPane) ((Button) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) root.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(seatView);
            } else {
                AlertUtils.showError("Error", "Main border pane not found!");
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Cannot load Booking View: " + e.getMessage());
            e.printStackTrace();
        }
    }
}