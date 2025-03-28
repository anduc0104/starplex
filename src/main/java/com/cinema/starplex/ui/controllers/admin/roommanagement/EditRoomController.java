package com.cinema.starplex.ui.controllers.admin.roommanagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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

    public void setRoom(Room room) {
        this.selectedRoom = room;
        roomNumberField.setText(String.valueOf(room.getRoomNumber()));
        totalSeatsField.setText(String.valueOf(room.getTotalSeats()));
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
        if (selectedRoom != null && validateFields()) {
            selectedRoom.setRoomNumber(Integer.parseInt(roomNumberField.getText()));
            selectedRoom.setTotalSeats(Integer.parseInt(totalSeatsField.getText()));

            try {
                roomDao.update(selectedRoom);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully");
                clearFields();
                returnToMovieView(event);
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

    @FXML
    private void handleBack(ActionEvent event) {
        returnToMovieView(event);
    }

    private void returnToMovieView(ActionEvent event) {
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

    public void handleClear(ActionEvent event) {
        roomNumberField.clear();
        totalSeatsField.clear();
    }

}
