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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditShowtimeController {

    @FXML
    private TextField starttimeField;
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

    private ShowTimeDao showtimeDAO;
    private MovieDao movieDAO;
    private RoomDao roomDAO;
    private static Showtime selectedShowtime;

    public EditShowtimeController() {
        this.showtimeDAO = new ShowTimeDao();
        this.movieDAO = new MovieDao();
        this.roomDAO = new RoomDao();
    }

    public static void setSelectedShowtime(Showtime showtime) {
        selectedShowtime = showtime;
    }

    @FXML
    public void initialize() throws SQLException {
        loadMovies();
        loadRooms();

        if (selectedShowtime != null) {
            starttimeField.setText(selectedShowtime.getStartTime().toLocalDateTime().toLocalTime().toString());
            priceField.setText(selectedShowtime.getPrice().toString());

            // Set giá trị mặc định trong ComboBox
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

    private void loadMovies() throws SQLException {
        List<Movie> movies = movieDAO.getMovies();
        ObservableList<Movie> movieList = FXCollections.observableArrayList(movies);
        movieComboBox.setItems(movieList);
    }

    private void loadRooms() {
        List<Room> rooms = roomDAO.getRooms();
        ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);
        roomComboBox.setItems(roomList);
    }

    @FXML
    private void handleEditShowtime(ActionEvent event) {
        try {
            // Định dạng giờ:phút
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(starttimeField.getText(), formatter);
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), time);
            Timestamp startTime = Timestamp.valueOf(dateTime);

            BigDecimal price = new BigDecimal(priceField.getText());

            // Lấy Movie và Room được chọn
            Movie selectedMovie = movieComboBox.getValue();
            Room selectedRoom = roomComboBox.getValue();

            if (selectedMovie == null || selectedRoom == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select both a movie and a room.");
                return;
            }

            // Kiểm tra id của Movie
            if (selectedMovie.getId() <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected movie does not have a valid ID.");
                return;
            }

            // Cập nhật chỉ các thuộc tính thay đổi
            boolean updated = false;

            // Chỉ cập nhật thời gian nếu nó khác
            if (starttimeField.getText() != null && !starttimeField.getText().isEmpty()) {
                LocalTime newTime = LocalTime.parse(starttimeField.getText(), formatter);
                if (!selectedShowtime.getStartTime().toLocalDateTime().toLocalTime().equals(newTime)) {
                    selectedShowtime.setStartTime(startTime);
                    updated = true;
                }
            }

            // Chỉ cập nhật giá nếu nó khác
            if (priceField.getText() != null && !priceField.getText().isEmpty()) {
                BigDecimal newPrice = new BigDecimal(priceField.getText());
                if (selectedShowtime.getPrice().compareTo(newPrice) != 0) {
                    selectedShowtime.setPrice(newPrice);
                    updated = true;
                }
            }

            // Chỉ cập nhật phim nếu khác
            if (selectedMovie != null) {
                if (!selectedShowtime.getMovie().equals(selectedMovie)) {
                    selectedShowtime.setMovie(selectedMovie);
                    updated = true;
                }
            }

            // Chỉ cập nhật phòng nếu khác
            if (selectedRoom != null) {
                if (!selectedShowtime.getRoom().equals(selectedRoom)) {
                    selectedShowtime.setRoom(selectedRoom);
                    updated = true;
                }
            }

            // Cập nhật vào DB chỉ khi có thay đổi
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
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid time format. Please use 'HH:mm'.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format. Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    @FXML
    private void handleEditClear() {
        starttimeField.clear();
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