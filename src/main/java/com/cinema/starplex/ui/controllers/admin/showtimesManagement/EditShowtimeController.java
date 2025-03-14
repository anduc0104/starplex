package com.cinema.starplex.ui.controllers.admin.showtimesManagement;

import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditShowtimeController {

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
    private static Showtime selectedShowtime;

    public EditShowtimeController() {
        this.showtimeDAO = new ShowTimeDao();
    }

    public static void setSelectedShowtime(Showtime showtime) {
        selectedShowtime = showtime;
    }

    @FXML
    public void initialize() {
        if (selectedShowtime != null) {
            starttimeField.setText(selectedShowtime.getStartTime().toString());
            priceField.setText(selectedShowtime.getPrice().toString());
        }
    }

    @FXML
    private void handleEditShowtime() {
        try {
            // Định dạng giờ:phút
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(starttimeField.getText(), formatter);

            // Lấy ngày hiện tại và kết hợp với giờ nhập vào
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), time);
            Timestamp startTime = Timestamp.valueOf(dateTime);

            // Chuyển đổi giá tiền
            BigDecimal price = new BigDecimal(priceField.getText());

            // Gán giá trị mới
            selectedShowtime.setStartTime(startTime);
            selectedShowtime.setPrice(price);

            // Cập nhật vào DB
            if (showtimeDAO.updateShowtime(selectedShowtime)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Showtime updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update showtime.");
            }
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid time format. Please use 'HH:mm'.");
            System.out.println("Input time: " + starttimeField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format. Please use a number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditClear() {
        starttimeField.clear();
        priceField.clear();
    }

    @FXML
    private void handleBack(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/showtimesmanagement/showtime-view.fxml");
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
