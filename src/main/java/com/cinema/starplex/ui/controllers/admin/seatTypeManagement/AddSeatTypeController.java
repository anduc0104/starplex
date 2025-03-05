package com.cinema.starplex.ui.controllers.admin.seatTypeManagement;

import com.cinema.starplex.dao.SeatTypeDao;
import com.cinema.starplex.models.SeatType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.math.BigDecimal;
import java.util.Optional;

public class AddSeatTypeController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;

    private final SeatTypeDao seatTypeDao = new SeatTypeDao();

    @FXML
    private void handleSave(ActionEvent event) {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            showAlert("Error", "Please enter complete information!");
            return;
        }

        // Kiểm tra xem giá có đúng định dạng số nguyên dương và tối đa 10 chữ số không
        if (!priceText.matches("\\d{1,10}")) {
            showAlert("Error", "Price must be a valid positive integer, maximum 10 digits!");
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceText);

            // Kiểm tra giá trị âm
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert("Error", "Price must be a positive integer!");
                return;
            }

            // Xác nhận trước khi lưu
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm");
            confirm.setHeaderText("Are you sure you want to add this chair type?");
            confirm.setContentText("Name: " + name + "\nPrice: " + price);
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                seatTypeDao.save(new SeatType(null, name, price, null));
                showAlert("Success", "New seat type has been added!");
                returnToSeatTypeView(event);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Price must be a valid integer!");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Xác nhận trước khi quay lại
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm return");
        confirm.setHeaderText("Are you sure you want to return?");
        confirm.setContentText("The entered data will not be saved.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToSeatTypeView(event);
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        nameField.clear();
        priceField.clear();
    }

    private void returnToSeatTypeView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/seattypemanagement/seat-type-view.fxml"));
            Parent seatTypeView = loader.load();

            // Thay đổi nội dung của BorderPane
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}