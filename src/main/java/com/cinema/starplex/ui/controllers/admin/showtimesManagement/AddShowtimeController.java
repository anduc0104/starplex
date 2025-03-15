package com.cinema.starplex.ui.controllers.admin.showtimesManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddShowtimeController {
    @FXML
    public ComboBox movieComboBox;
    @FXML
    public ComboBox roomComboBox;
    @FXML
    private TextField starttimeField;
    @FXML
    private TextField priceField;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button backButton;

    private ShowTimeDao showtimeDAO;
    private final RoomDao roomDao = new RoomDao();
    private final MovieDao movieDao = new MovieDao();

    @FXML
    public void initialize() {
        roomComboBox.getItems().addAll(roomDao.findAll());
        movieComboBox.getItems().addAll(movieDao.findAll());

    }

    public AddShowtimeController() {
        this.showtimeDAO = new ShowTimeDao();
    }

    @FXML
    private void handleAddShowtime(ActionEvent event) {
        try {
            // Format time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(starttimeField.getText(), formatter);
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), time);
            Timestamp startTime = Timestamp.valueOf(dateTime);

            // Get ticket price
            BigDecimal price = new BigDecimal(priceField.getText());

            // Create Showtime object (Movie and Room set to null for now)
            Showtime showtime = new Showtime(null, null, null, startTime, price, null);

            // Insert Showtime using DAO
            if (showtimeDAO.insertShowtime(showtime)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Showtime added successfully.");
                handleClear();
                returnToShowtimeView(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add showtime.");
            }
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid time format. Please use HH:mm.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid price value.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    @FXML
    private void handleClear() {
        starttimeField.clear();
        priceField.clear();
    }

    @FXML
    private void handleBack(ActionEvent actionEvent) {
        returnToShowtimeView(actionEvent);
    }

    private void returnToShowtimeView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/showtimesmanagement/showtime-view.fxml"));
            Parent seatTypeView = loader.load();

            // Change the content of BorderPane
            AnchorPane root = (AnchorPane) ((Button) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) root.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(seatTypeView);
            } else {
                System.err.println("Could not find BorderPane with ID 'mainBorderPane'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
