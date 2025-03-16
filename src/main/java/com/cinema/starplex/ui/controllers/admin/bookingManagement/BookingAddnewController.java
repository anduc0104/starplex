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

    private ObservableList<Booking> bookingList;
    private final BookingDao bookingDao = new BookingDao();
    private final UserDao userDao = new UserDao();
    private final ShowTimeDao showtimeDao = new ShowTimeDao();
    private final PaymentDao paymentDao = new PaymentDao();

    public void initialize() {
        // Lấy dữ liệu từ CSDL
        userComboBox.getItems().addAll(userDao.findAll());
        showtimeComboBox.getItems().addAll(showtimeDao.findAll());
        // Hiển thị roomNumber trong ComboBox
        // Gọi loadBookings() để lấy dữ liệu từ DB
        loadUsers();
        loadShowtimes();
        loadStatusOptions();
    }

    private void loadUsers() {
        List<User> users = userDao.findAll();
        System.out.println("Users loaded: " + users.size());
        for (User user : users) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + ", Email: " + user.getEmail());
        }
        userComboBox.setItems(FXCollections.observableArrayList(users));
        // Set a cell factory to display username in ComboBox
        userComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + " | " +
                            "User: " + item.getUsername() + " | " +
                            "Email: " + item.getEmail() + " | ");
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
                    setText("ID: " + item.getId() + " | " +
                            "User: " + item.getUsername() + " | " +
                            "Email: " + item.getEmail() + " | ");
                }
            }
        });
        userComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return (user != null) ? user.getId() + "-" + user.getUsername() + " - " + user.getEmail() : "";
            }

            @Override
            public User fromString(String string) {
                return null;
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
        showtimeComboBox.setConverter(new StringConverter<Showtime>() {
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
                    Payment payment = new Payment();
                    payment.setBooking(booking);
                    payment.setAmount(totalPrice);
                    payment.setPaymentMethod("Credit Card");
                    payment.setStatus("Pending"); // Đợi xác nhận thanh toán

                    boolean paymentSuccess = paymentDao.insert(payment);

                    if (paymentSuccess) {
                        AlertUtils.showInfo("Success", "Booking and Payment added successfully!");
                        returnToBookingView(event);
                    } else {
                        AlertUtils.showError("Error", "Booking created but failed to add payment!");
                    }
//                    updateStatistics();
                    returnToBookingView(event);
                } else {
                    AlertUtils.showError("Error", "Failed to add booking!");
                }
            } catch (Exception e) {
                AlertUtils.showInfo("Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
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
            AlertUtils.showWarning("Warning", "Validation Error");
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
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
