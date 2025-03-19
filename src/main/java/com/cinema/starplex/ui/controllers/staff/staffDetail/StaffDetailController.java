package com.cinema.starplex.ui.controllers.staff.staffDetail;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.SeatDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class StaffDetailController {
    @FXML
    private GridPane seatGrid;
    @FXML
    private Label totalPriceLabel;
    private final RoomDao roomDao = new RoomDao();
    private final SeatDao seatDao = new SeatDao();
    @FXML
    private Map<Integer, SeatType> seatTypes = new HashMap<>();
    @FXML
    private ComboBox<Room> roomSelector;
    @FXML
    private Label totalSeatsLabel;
    @FXML
    private Label selectedSeatsLabel;
    @FXML
    private List<Seat> selectedSeats = new ArrayList<>();

    public StaffDetailController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the seat types map
        if (seatGrid != null) {
            seatGrid.getChildren().clear();
        } else {
            System.out.println("seatGrid is null! Check FXML file.");
        }
        System.out.println("StaffDetailController initialized!");
        // Load rooms into the combo box
        loadRooms();
    }

    // load dsach loai ghe tu db
    public void loadSeatTypes() {
//        seatTypes = roomDao.getSeatTypes();
    }

    // load dsach hong tu db va cap nhat UI
    public void loadRooms() {
        List<Room> rooms = roomDao.getRooms();
        ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);

        roomSelector.setItems(roomList);
        if (!roomList.isEmpty()) {
            roomSelector.setValue(roomList.get(0));
            loadSeatsForRoom(roomList.get(0).getId());
            updateTotalSeatsLabel();
        }
    }

    private void loadSeatsForRoom(int roomId) {
//       List<Seat> seats = roomDao.getSeatsForRoom(roomId);
//
//       // Xóa grid và danh sách chọn
//       seatGrid.getChildren().clear();
//       selectedSeats.clear();
//
//       // Xác định bố cục grid (row và column)
//       Map<Character, Integer> rows = new TreeMap<>();
//       Map<Integer, Integer> columns = new TreeMap<>();
//
//       for (Seat seat : seats) {
//           rows.put(seat.getRow(), rows.getOrDefault(seat.getRow(), 0) + 1);
//           columns.put(seat.getColumn(), columns.getOrDefault(seat.getColumn(), 0) + 1);
//       }
//
//       // Tạo tiêu đề cột (column numbers)
//       for (Integer col : columns.keySet()) {
//           Label colLabel = new Label(String.valueOf(col));
//           colLabel.setPrefWidth(30);
//           colLabel.setAlignment(Pos.CENTER);
//           seatGrid.add(colLabel, col, 0);
//       }
//
//       // Tạo tiêu đề hàng (row letters)
//       for (Character row : rows.keySet()) {
//           Label rowLabel = new Label(String.valueOf(row));
//           rowLabel.setPrefWidth(30);
//           rowLabel.setAlignment(Pos.CENTER);
//           seatGrid.add(rowLabel, 0, row - 'A' + 1);
//       }
//
//       // Thêm ghế vào grid
//       for (Seat seat : seats) {
//           Button seatButton = new Button(seat.getSeatNumber());
//           seatButton.setPrefSize(30, 30);
//
//           // Áp dụng kiểu dựa vào seat type
//           SeatType type = seatTypes.get(seat.getSeatType());
//           if (type != null) {
//               seatButton.getStyleClass().add("seat-" + type.getName().toLowerCase());
//           } else {
//               seatButton.getStyleClass().add("seat-normal");
//           }
//
//           // Nếu ghế đã được booking, disable ghế đó
//           if (seat.isBooked()) {
//               seatButton.setDisable(true);
//               seatButton.getStyleClass().add("seat-booked");
//           }
//
//           // Gán sự kiện click vào ghế
//           seatButton.setOnAction(e -> toggleSeatSelection(seat, seatButton));
//
//           // Thêm vào grid
//           seatGrid.add(seatButton, seat.getColumn(), seat.getRow() - 'A' + 1);
//       }
    }

    private void updateTotalSeatsLabel() {
        Room selectedRoom = roomSelector.getValue();
        if (selectedRoom != null) {
            totalSeatsLabel.setText("Total Seats: " + selectedRoom.getTotalSeats());
        } else {
            totalSeatsLabel.setText("Total Seats: 0");
        }
    }

    private void processPayment() {
        if (selectedSeats.isEmpty()) {
            showAlert("No Seats Selected", "Please select at least one seat before proceeding to payment.");
            return;
        }

        // Here you would typically open a payment dialog or proceed to payment
        // processing
        // just show a confirmation dialog

        BigDecimal totalPrice = BigDecimal.ZERO;
        StringBuilder seatList = new StringBuilder();

        for (Seat seat : selectedSeats) {
            SeatType type = seatTypes.get(seat.getSeatType());
            if (type != null) {
                totalPrice = totalPrice.add(type.getPrice()); // Cộng giá bằng BigDecimal
                seatList.append(seat.getSeatNumber())
                        .append(" (").append(type.getName())
                        .append(": ").append(type.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP))
                        .append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Booking");
        alert.setHeaderText("Please confirm your seat booking");
        alert.setContentText("Selected seats:\n" + seatList.toString() +
                "\nTotal Price: " + String.format("%.2f", totalPrice));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Implement booking logic here
                // This would typically involve inserting records into a bookings table
                // and then refreshing the display
                showAlert("Booking Successful", "Your seats have been booked successfully!");
                loadSeatsForRoom(roomSelector.getValue().getId());
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
