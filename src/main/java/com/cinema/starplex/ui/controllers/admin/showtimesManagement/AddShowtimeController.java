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

public class AddShowtimeController {

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

    public AddShowtimeController() {
        this.showtimeDAO = new ShowTimeDao();
    }

    @FXML
    private void handleAddShowtime() {
        try {
            // Định dạng giờ và phút
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(starttimeField.getText(), formatter);
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), time);
            Timestamp startTime = Timestamp.valueOf(dateTime);

            // Lấy giá trị giá vé
            BigDecimal price = new BigDecimal(priceField.getText());

            // Tạo đối tượng Showtime (giữ nguyên giá trị null nếu chưa cần Movie và Room)
            Showtime showtime = new Showtime(null, null, null, startTime, price, null);

            // Gọi DAO để thêm suất chiếu
            if (showtimeDAO.insertShowtime(showtime)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm suất chiếu thành công.");
                handleClear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm suất chiếu.");
            }
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Định dạng thời gian không hợp lệ. Hãy nhập theo HH:mm.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Giá vé không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra.");
        }
    }


    @FXML
    private void handleClear() {
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
