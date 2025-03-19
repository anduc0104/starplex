package com.cinema.starplex.ui.controllers.admin.seatManagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.SeatDao;
import com.cinema.starplex.dao.SeatTypeDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import javafx.collections.FXCollections;
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

public class EditSeatController {
    @FXML
    private ComboBox<Room> roomComboBox;
    @FXML
    private ComboBox<SeatType> seatTypeComboBox;
    @FXML
    private TextField rowField; // Thay thế seatNumberField bằng rowField
    @FXML
    private TextField colField; // Thêm colField để nhập số cột
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;

    private final SeatDao seatDao = new SeatDao();
    private final RoomDao roomDao = new RoomDao();
    private final SeatTypeDao seatTypeDao = new SeatTypeDao();

    private Seat selectedSeat;

    public void setSeat(Seat seat) {
        this.selectedSeat = seat;
        roomComboBox.setItems(FXCollections.observableArrayList(roomDao.findAll()));
        seatTypeComboBox.setItems(FXCollections.observableArrayList(seatTypeDao.findAll()));

        setupComboBoxes(); // Cấu hình hiển thị dữ liệu

        if (seat != null) {
            roomComboBox.setValue(seat.getRoom());
            seatTypeComboBox.setValue(seat.getSeatType());
            rowField.setText(String.valueOf(seat.getRow())); // Cập nhật hàng
            colField.setText(String.valueOf(seat.getCol_number())); // Cập nhật cột
        }
    }

    private void setupComboBoxes() {
        // Hiển thị roomNumber trong ComboBox phòng
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
        if (selectedSeat == null) {
            showAlert("Error", "No seat information found to edit!");
            return;
        }

        Room selectedRoom = roomComboBox.getValue();
        SeatType selectedSeatType = seatTypeComboBox.getValue();
        String row = rowField.getText().trim();
        String col = colField.getText().trim();

        if (selectedRoom == null || selectedSeatType == null || row.isEmpty() || col.isEmpty()) {
            showAlert("Error", "Please enter complete information!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm");
        confirm.setHeaderText("Are you sure you want to update this seat information?");
        confirm.setContentText("Room: " + selectedRoom.getRoomNumber() + "\nSeat Type: " + selectedSeatType.getName() + "\nRow: " + row + "\nColumn: " + col);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedSeat.setRoom(selectedRoom);
            selectedSeat.setSeatType(selectedSeatType);
            selectedSeat.setRow(row); // Cập nhật hàng
            selectedSeat.setCol_number(Integer.parseInt(col)); // Cập nhật cột
            selectedSeat.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            seatDao.update(selectedSeat);
            showAlert("Success", "Seat information has been updated!");
            returnToSeatView(event);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        returnToSeatView(event);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        rowField.clear();
        colField.clear();
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