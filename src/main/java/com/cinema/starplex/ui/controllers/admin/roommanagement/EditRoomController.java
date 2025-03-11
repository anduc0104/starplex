package com.cinema.starplex.ui.controllers.admin.roommanagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class EditRoomController {
    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField totalSeatsField;
    private Room selectedRoom;

    private RoomDao roomDao;

    public void initialize() {
        roomDao = new RoomDao();
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
    private void handleUpdateButton(ActionEvent event) {
        if (selectedRoom != null && validateFields()) {
            selectedRoom.setRoomNumber(Integer.parseInt(roomNumberField.getText()));
            selectedRoom.setTotalSeats(Integer.parseInt(totalSeatsField.getText()));

            try {
                roomDao.update(selectedRoom);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully");
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        roomNumberField.clear();
        totalSeatsField.clear();
        selectedRoom = null;
    }

    // hien thi du lieu phong vao cac text field
    private void populateFields(Room room) {
        roomNumberField.setText(String.valueOf(room.getRoomNumber()));
        totalSeatsField.setText(String.valueOf(room.getTotalSeats()));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
