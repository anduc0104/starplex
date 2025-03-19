package com.cinema.starplex.ui.controllers.admin.paymentManagement;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.PaymentDao;
import com.cinema.starplex.models.*;
import com.cinema.starplex.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import java.math.BigDecimal;
import java.util.List;

public class PaymentEditController {
    @FXML
    private Button backButton, saveButton, clearButton;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<Booking> bookingComboBox;

    @FXML
    private ComboBox<String> paymentComboBox, statusComboBox;

    private final PaymentDao paymentDao = new PaymentDao();
    private final BookingDao bookingDao = new BookingDao();
    private Payment currentPayment;

    @FXML
    public void initialize() {
        loadBookings();
        loadPaymentMethods();
        loadStatusOptions();
    }

    public void setPayment(Payment payment) {
        this.currentPayment = payment;
        bookingComboBox.setItems(FXCollections.observableArrayList(bookingDao.findAll()));
        setupComboBoxes();
        if (payment != null) {
            currentPayment = payment;
            amountField.setText(String.valueOf(payment.getAmount()));
            bookingComboBox.setValue(payment.getBooking());
            paymentComboBox.setValue(payment.getPaymentMethod());
            statusComboBox.setValue(payment.getStatus());
        }
    }

    private void setupComboBoxes() {
        // Hiển thị thông tin booking
        bookingComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (booking == null || empty) {
                    setText(null);
                } else {
                    setText("ID: " + booking.getId() + " | " +
                            "User: " + booking.getUser().getUsername() + " | " +
                            "Showtime: " + booking.getShowtime() + " | " +
                            "Price: $" + booking.getTotalPrice() + " | " +
                            "Status: " + booking.getStatus());
                }
            }
        });

        bookingComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Booking booking) {
                return (booking != null) ? "Booking #" + booking.getId() + " - " + booking.getUser() : "";
            }

            @Override
            public Booking fromString(String string) {
                return null;
            }
        });

        // Gọi loadBookings() để lấy dữ liệu từ DB
        loadBookings();
    }


    private void loadBookings() {
        BookingDao bookingDao = new BookingDao();
        List<Booking> bookings = bookingDao.findAll();
        bookingComboBox.setItems(FXCollections.observableArrayList(bookings));
    }


    private void loadPaymentMethods() {
        List<String> methods = List.of("Credit Card", "PayPal", "Cash", "Bank Transfer");
        paymentComboBox.setItems(FXCollections.observableArrayList(methods));
    }

    private void loadStatusOptions() {
        List<String> statuses = List.of("Pending", "Completed", "Failed", "Refunded");
        statusComboBox.setItems(FXCollections.observableArrayList(statuses));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentPayment == null) {
            AlertUtils.showError("Error", "No payment selected for editing.");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            Booking booking = bookingComboBox.getValue();
            String paymentMethod = paymentComboBox.getValue();
            String status = statusComboBox.getValue();

            if (booking == null || paymentMethod == null || status == null ) {
                AlertUtils.showError("Validation Error", "All fields must be filled.");
                return;
            }

            AlertUtils.showWarning("Warning", "Are you sure you want to update this payment information?");

            currentPayment.setAmount(amount);
            currentPayment.setBooking(booking);
            currentPayment.setPaymentMethod(paymentMethod);
            currentPayment.setStatus(status);

            paymentDao.update(currentPayment);
            AlertUtils.showInfo("Success", "Payment updated successfully.");
            returnToPaymentView(event);
        } catch (NumberFormatException e) {
            AlertUtils.showError("Validation Error", "Amount must be a valid number.");
        }
    }

    @FXML
    private void handleClear() {
        amountField.clear();
        bookingComboBox.getSelectionModel().clearSelection();
        paymentComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
    }


    @FXML
    private void handleBack(ActionEvent event) {
        returnToPaymentView(event);
    }

    private void returnToPaymentView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/paymentmanagement/payment-view.fxml"));
            Parent seatView = loader.load();

            AnchorPane root = (AnchorPane) ((Button) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) root.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(seatView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}