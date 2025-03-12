package com.cinema.starplex.ui.controllers.admin.roommanagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class AddnewRoomController {
    @FXML
    private Button clearButton;

    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField totalSeatsField;
    @FXML
    private Button saveButton;
    private RoomDao roomDao;
    private Room selectedRoom;

    public void initialize() {
        roomDao = new RoomDao();
    }

    public void handleBack(ActionEvent event) {
        returnToRoomView(event);
    }

    private void returnToRoomView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/roommanagement/list-room.fxml"));
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

    private boolean validateFields() {
        String roomNumberText = roomNumberField.getText();
        String totalSeatsText = totalSeatsField.getText();

        if (roomNumberText.isEmpty() || totalSeatsText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required");
            return false;
        }

        try {
            int roomNumber = Integer.parseInt(roomNumberText);
            int totalSeats = Integer.parseInt(totalSeatsText);

            if (roomNumber <= 0 || totalSeats <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Room number and total seats must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Room number and total seats must be numeric");
            return false;
        }

        return true;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateFields()) {
            Room room = new Room();
            room.setRoomNumber(Integer.parseInt(roomNumberField.getText()));
            room.setTotalSeats(Integer.parseInt(totalSeatsField.getText()));

            try {
                boolean success = roomDao.insert(room);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room saved successfully");
                    handleClear();
                    returnToRoomView(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save room");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void handleClear() {
        roomNumberField.clear();
        totalSeatsField.clear();
    }
}
