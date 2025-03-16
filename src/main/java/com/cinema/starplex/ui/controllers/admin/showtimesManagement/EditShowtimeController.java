package com.cinema.starplex.ui.controllers.admin.showtimesManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditShowtimeController {

    @FXML
    private DatePicker showDatePicker;
    @FXML
    private TextField showTimePicker;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<Movie> movieComboBox;
    @FXML
    private ComboBox<Room> roomComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button backButton;

    private final ShowTimeDao showtimeDAO = new ShowTimeDao();
    private final MovieDao movieDAO = new MovieDao();
    private final RoomDao roomDAO = new RoomDao();
    private static Showtime selectedShowtime;

    public static void setSelectedShowtime(Showtime showtime) {
        selectedShowtime = showtime;
    }

    @FXML
    public void initialize() {
        loadMovies();
        loadRooms();

        if (selectedShowtime != null) {
            showDatePicker.setValue(selectedShowtime.getShowDate().toLocalDate());
            showTimePicker.setText(selectedShowtime.getShowTime().toLocalTime().toString());
            priceField.setText(selectedShowtime.getPrice().toString());

            if (selectedShowtime.getMovie() != null) {
                movieComboBox.setValue(selectedShowtime.getMovie());
            }
            if (selectedShowtime.getRoom() != null) {
                roomComboBox.setValue(selectedShowtime.getRoom());
            }
        } else {
            System.out.println("Warning: No showtime selected.");
        }
    }

    private void loadMovies() {
        List<Movie> movies = movieDAO.findAll();
        ObservableList<Movie> movieList = FXCollections.observableArrayList(movies);
        movieComboBox.setItems(movieList);
    }

    private void loadRooms() {
        List<Room> rooms = roomDAO.findAll();
        ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);
        roomComboBox.setItems(roomList);
    }

    @FXML
    private void handleEditShowtime(ActionEvent event) {
        try {
            if (selectedShowtime == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No showtime selected.");
                return;
            }

            // Lấy ngày chiếu từ DatePicker
            LocalDate showDate = showDatePicker.getValue();
            if (showDate == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a show date.");
                return;
            }
            Date sqlShowDate = Date.valueOf(showDate);

            // Lấy giờ chiếu từ TextField
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime showTime = LocalTime.parse(showTimePicker.getText(), formatter);
            Time sqlShowTime = Time.valueOf(showTime);

            // Lấy giá vé
            BigDecimal price = new BigDecimal(priceField.getText());

            // Lấy Movie và Room được chọn
            Movie selectedMovie = movieComboBox.getValue();
            Room selectedRoom = roomComboBox.getValue();

            if (selectedMovie == null || selectedRoom == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select both a movie and a room.");
                return;
            }

            // Kiểm tra sự thay đổi
            boolean updated = false;

            if (!selectedShowtime.getShowDate().equals(sqlShowDate)) {
                selectedShowtime.setShowDate(sqlShowDate);
                updated = true;
            }

            if (!selectedShowtime.getShowTime().toLocalTime().equals(showTime)) {
                selectedShowtime.setShowTime(sqlShowTime);
                updated = true;
            }

            if (selectedShowtime.getPrice().compareTo(price) != 0) {
                selectedShowtime.setPrice(price);
                updated = true;
            }

            if (!selectedShowtime.getMovie().equals(selectedMovie)) {
                selectedShowtime.setMovie(selectedMovie);
                updated = true;
            }

            if (!selectedShowtime.getRoom().equals(selectedRoom)) {
                selectedShowtime.setRoom(selectedRoom);
                updated = true;
            }

            // Chỉ cập nhật nếu có thay đổi
            if (updated) {
                if (showtimeDAO.updateShowtime(selectedShowtime)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Showtime updated successfully.");
                    returnToShowtimeView(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update showtime.");
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Info", "No changes detected.");
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
        showDatePicker.setValue(null);
        showTimePicker.clear();
        priceField.clear();
        movieComboBox.setValue(null);
        roomComboBox.setValue(null);
    }

    @FXML
    private void handleBack(ActionEvent actionEvent) {
        returnToShowtimeView(actionEvent);
    }

    private void returnToShowtimeView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/showtimesmanagement/showtime-view.fxml"));
            Parent showtimeView = loader.load();

            AnchorPane root = (AnchorPane) ((Button) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) root.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(showtimeView);
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