package com.cinema.starplex.ui.controllers.admin.seatManagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.SeatDao;
import com.cinema.starplex.dao.SeatTypeDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import java.sql.Timestamp;
import java.util.Optional;

public class AddSeatController {
    @FXML
    private ComboBox<Room> roomComboBox;
    @FXML
    private ComboBox<SeatType> seatTypeComboBox;
    @FXML
    private TextField seatNumberField;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;

    private final SeatDao seatDao = new SeatDao();
    private final RoomDao roomDao = new RoomDao();
    private final SeatTypeDao seatTypeDao = new SeatTypeDao();

    @FXML
    public void initialize() {
        // Lấy dữ liệu từ CSDL
        roomComboBox.getItems().addAll(roomDao.findAll());
        seatTypeComboBox.getItems().addAll(seatTypeDao.findAll());

        // Hiển thị roomNumber trong ComboBox
        roomComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                setText((room == null || empty) ? "" : String.valueOf(room.getRoomNumber()));
            }
        });

        roomComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Room room) {
                return (room != null) ? String.valueOf(room.getRoomNumber()) : "";
            }

            @Override
            public Room fromString(String string) {
                return null;
            }
        });

        // Hiển thị name trong ComboBox loại ghế
        seatTypeComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SeatType seatType, boolean empty) {
                super.updateItem(seatType, empty);
                setText((seatType == null || empty) ? "" : seatType.getName());
            }
        });

        seatTypeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SeatType seatType) {
                return (seatType != null) ? seatType.getName() : "";
            }

            @Override
            public SeatType fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleSave(ActionEvent event) {
        Room selectedRoom = roomComboBox.getValue();
        SeatType selectedSeatType = seatTypeComboBox.getValue();
        String seatNumber = seatNumberField.getText().trim();

        if (selectedRoom == null || selectedSeatType == null || seatNumber.isEmpty()) {
            showAlert("Error", "Please enter complete information!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm");
        confirm.setHeaderText("Are you sure you want to add this seat?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Seat newSeat = new Seat(null, selectedRoom, selectedSeatType, seatNumber, new Timestamp(System.currentTimeMillis()));
            seatDao.save(newSeat);
            showAlert("Success", "Chairs have been added!");
            returnToSeatView(event);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm exit");
        confirm.setHeaderText("Are you sure you want to exit?");
        confirm.setContentText("Changes will not be saved.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToSeatView(event);
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        seatNumberField.clear();
        roomComboBox.getSelectionModel().clearSelection();
        seatTypeComboBox.getSelectionModel().clearSelection();
    }

    private void returnToSeatView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/seatmanagement/seat-view.fxml"));
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}