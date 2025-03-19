package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.BookingDetailDao;
import com.cinema.starplex.dao.PaymentDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.session.SessionManager;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class SelectPaymentMethod {
    @FXML
    public Label movieTitle;
    @FXML
    public Label movieShowtime;
    @FXML
    public Label listSeatSelected;
    @FXML
    public Label roomNumber;
    @FXML
    public Label totalPrice;
    @FXML
    public Label categoryCol;
    @FXML
    public Label quantityCol;
    @FXML
    public Label totalAmountCol;
    @FXML
    public Button btnPayment;

    public Movie selectedMovie;
    public Showtime selectedShowtime;
    public List<Seat> selectedSeats;
    public double totalPriceToPayment;
    public String paymentMethod;

    public ShowTimeDao showTimeDao;
    public BookingDao bookingDao;
    public BookingDetailDao bookingDetailDao;
    public PaymentDao paymentDao;


    public void initialize() {
        showTimeDao = new ShowTimeDao();
        bookingDao = new BookingDao();
        bookingDetailDao = new BookingDetailDao();
        paymentDao = new PaymentDao();
    }

    public void setInfo(Movie selectedMovie, Showtime selectedShowtime, List<Seat> selectedSeats, double totalPriceToPayment) {
        this.selectedMovie = selectedMovie;
        this.selectedShowtime = selectedShowtime;
        this.selectedSeats = selectedSeats;
        this.totalPriceToPayment = totalPriceToPayment;

//        set movie title
        movieTitle.setText(selectedMovie.getTitle());
//        set showtime
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Time show_time = selectedShowtime.getShowTime();
        String formattedTime = timeFormat.format(new Date(show_time.getTime()));

        movieShowtime.setText(formattedTime);
//    set room number
        roomNumber.setText(String.valueOf(showTimeDao.getRoomNumber(selectedShowtime.getRoomId())));
//    set list seat selected
        StringBuilder seatNumbers = new StringBuilder();
        for (Seat seat : selectedSeats) {
            String seatName = seat.getRow() + String.valueOf(seat.getCol_number());
            seatNumbers.append(seatName).append(", ");

        }
        listSeatSelected.setText(seatNumbers.toString().substring(0, seatNumbers.length() - 2));
        totalPrice.setText(formatCurrency(totalPriceToPayment));

        categoryCol.setText("Seat(" + seatNumbers.toString().substring(0, seatNumbers.length() - 2) + ")");
        quantityCol.setText(String.valueOf(selectedSeats.size()));
        totalAmountCol.setText(formatCurrency(totalPriceToPayment));
    }

    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,###,### đ");
        return df.format(amount);
    }

    public void selectPaymentMethod(ActionEvent event) {
        btnPayment.setDisable(!btnPayment.isDisable());
    }

    public void handleBack(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("staff/movie_detail_layout.fxml");
        if (loader != null) {
            MovieDetailLayout controller = loader.getController();
            try {
                controller.setCenterBorderPane(selectedShowtime, selectedMovie);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load add-seat.fxml");
        }
    }


    public void handlePayment(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Payment Confirmation");
        alert.setHeaderText("Are you sure you want to proceed with the payment?");
        alert.setContentText("Click OK to confirm or Cancel to abort.");

        // Show the alert and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK -> Process the payment
            processPayment(event);
        } else {
            // User clicked Cancel or closed the alert
            System.out.println("Payment was canceled.");
        }
    }

    private void processPayment(ActionEvent event) throws SQLException {
//        System.out.println(SessionManager.getInstance().getUserId());
        try {
            int bookingId = bookingDao.createBookingPayment(new Booking(SessionManager.getInstance().getUserId(), selectedShowtime.getId(), selectedSeats.size(), totalPriceToPayment));
//        System.out.println(bookingId);
            if (bookingId == -1) {
                throw new SQLException("Failed to create booking");
            }
            bookingDetailDao.createBookingDetail(bookingId, selectedSeats);

            paymentDao.creaetePaymentDetails(bookingId, totalPriceToPayment);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText("Payment has been successful.");
            alert.setContentText("Your booking ID: " + bookingId);
            alert.showAndWait();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            SceneSwitcher.switchTo(new Stage(), "staff/main-layout.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}