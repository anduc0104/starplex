package com.cinema.starplex.ui.controllers.staff.staffDetail;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.SeatDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.models.Showtime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
    private int totalPrice = 0;

    public StaffDetailController() {
    }

    //    private final Map<String, Integer> seatPrices = SeatLoadPrice.loadSeatPrice();
//    private int totalPrice = 0;

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

    public HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(15));
        topSection.setAlignment(Pos.CENTER_LEFT);

        Label roomLabel = new Label("Select Room:");
        roomSelector = new ComboBox<>();
        roomSelector.setPrefWidth(200);
        roomSelector.setOnAction(e -> {
            if (roomSelector.getValue().getRoomNumber() != null) {
                loadSeatsForRoom(roomSelector.getValue().getId());
                updateTotalSeatsLabel();
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> {
            loadRooms();
            clearSelection();
        });

        Button manageRoomsButton = new Button("Manage Rooms");
        manageRoomsButton.setOnAction(e -> openRoomManagementDialog());

        topSection.getChildren().addAll(roomLabel, roomSelector, refreshButton, manageRoomsButton);
        return topSection;
    }

    public VBox createBottomSection() {
        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(15));
        bottomSection.setAlignment(Pos.CENTER);

        // Legend
        HBox legend = new HBox(10);
        legend.setAlignment(Pos.CENTER);

        String[] types = {"Normal", "VIP", "Booked", "Double", "Selected"};
        String[] styleClasses = {"seat-normal", "seat-vip", "seat-booked", "seat-double", "seat-selected"};

        for (int i = 0; i < types.length; i++) {
            VBox legendItem = new VBox(5);
            legendItem.setAlignment(Pos.CENTER);

            Label typeLabel = new Label("Seat " + types[i]);
            typeLabel.getStyleClass().add("legend-label");

            Button sampleButton = new Button();
            sampleButton.getStyleClass().add(styleClasses[i]);
            sampleButton.setPrefSize(30, 30);

            if (types[i].equals("Booked")) {
                sampleButton.setDisable(true);
            }

            legendItem.getChildren().addAll(typeLabel, sampleButton);
            legend.getChildren().add(legendItem);
        }

        // Information section
        HBox infoSection = new HBox(20);
        infoSection.setAlignment(Pos.CENTER);

        totalSeatsLabel = new Label("Total Seats: 0");
        selectedSeatsLabel = new Label("Selected Seats: 0");
        totalPriceLabel = new Label("Total Price: 0.00");

        VBox infoLabels = new VBox(10);
        infoLabels.getChildren().addAll(totalSeatsLabel, selectedSeatsLabel, totalPriceLabel);

        Button paymentButton = new Button("Payment");
        paymentButton.getStyleClass().add("payment-button");
        paymentButton.setOnAction(e -> processPayment());

        infoSection.getChildren().addAll(infoLabels, paymentButton);

        bottomSection.getChildren().addAll(legend, infoSection);
        return bottomSection;
    }

    //load dsach loai ghe tu db
    public void loadSeatTypes() {
        seatTypes = roomDao.getSeatTypes();
    }

    //load dsach hong tu db va cap nhat UI
    public void loadRooms() {
        List<Room> rooms = roomDao.getRooms();
        ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);

        roomSelector.setItems(roomList);
        if (!roomList.isEmpty()) {
            roomSelector.setValue(roomList.get(0));
            loadSeatsForRoom(roomList.get(0).getId());
            updateTotalSeatsLabel();
        }
        roomSelector.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                return (room != null) ? String.valueOf(room.getRoomNumber()) : "";
            }

            @Override
            public Room fromString(String string) {
                return roomSelector.getItems().stream()
                        .filter(room -> String.valueOf(room.getRoomNumber()).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

    }

    private void loadSeatsForRoom(int roomId) {
        List<Seat> seats = roomDao.getSeatsForRoom(roomId);

        // Xóa grid và danh sách chọn
        seatGrid.getChildren().clear();
        selectedSeats.clear();

        // Xác định bố cục grid (row và column)
        Map<Character, Integer> rows = new TreeMap<>();
        Map<Integer, Integer> columns = new TreeMap<>();

        for (Seat seat : seats) {
            rows.put(seat.getRow(), rows.getOrDefault(seat.getRow(), 0) + 1);
            columns.put(seat.getColumn(), columns.getOrDefault(seat.getColumn(), 0) + 1);
        }

        // Tạo tiêu đề cột (column numbers)
        for (Integer col : columns.keySet()) {
            Label colLabel = new Label(String.valueOf(col));
            colLabel.setPrefWidth(30);
            colLabel.setAlignment(Pos.CENTER);
            seatGrid.add(colLabel, col, 0);
        }

        // Tạo tiêu đề hàng (row letters)
        for (Character row : rows.keySet()) {
            Label rowLabel = new Label(String.valueOf(row));
            rowLabel.setPrefWidth(30);
            rowLabel.setAlignment(Pos.CENTER);
            seatGrid.add(rowLabel, 0, row - 'A' + 1);
        }

        // Thêm ghế vào grid
        for (Seat seat : seats) {
            Button seatButton = new Button(seat.getSeatNumber());
            seatButton.setPrefSize(30, 30);

            // Áp dụng kiểu dựa vào seat type
            SeatType type = seatTypes.get(seat.getSeatType());
            if (type != null) {
                seatButton.getStyleClass().add("seat-" + type.getName().toLowerCase());
            } else {
                seatButton.getStyleClass().add("seat-normal");
            }

            // Nếu ghế đã được booking, disable ghế đó
            if (seat.isBooked()) {
                seatButton.setDisable(true);
                seatButton.getStyleClass().add("seat-booked");
            }

            // Gán sự kiện click vào ghế
            seatButton.setOnAction(e -> toggleSeatSelection(seat, seatButton));

            // Thêm vào grid
            seatGrid.add(seatButton, seat.getColumn(), seat.getRow() - 'A' + 1);
        }
    }

    private void openRoomManagementDialog() {
        // Create a dialog for room management
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Room Management");
        dialog.setHeaderText("Manage Rooms and Seats");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the grid for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Room list view
        ListView<Room> roomListView = new ListView<>();
        roomListView.setPrefHeight(200);
        roomListView.setItems(roomSelector.getItems());

        // Room details
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Room Number");

        TextField totalSeatsField = new TextField();
        totalSeatsField.setPromptText("Total Seats");

        Button addRoomButton = new Button("Add Room");
        Button updateRoomButton = new Button("Update Room");
        Button deleteRoomButton = new Button("Delete Room");
        Button manageSeatsButton = new Button("Manage Seats");

        // Add elements to the grid
        grid.add(new Label("Rooms:"), 0, 0);
        grid.add(roomListView, 0, 1, 2, 1);

        grid.add(new Label("Room Number:"), 0, 2);
        grid.add(roomNumberField, 1, 2);

        grid.add(new Label("Total Seats:"), 0, 3);
        grid.add(totalSeatsField, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addRoomButton, updateRoomButton, deleteRoomButton, manageSeatsButton);
        grid.add(buttonBox, 0, 4, 2, 1);

        // Event handling khi chon 1 phong trong dsach
        roomListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                roomNumberField.setText(String.valueOf(newVal.getRoomNumber()));
                totalSeatsField.setText(String.valueOf(newVal.getTotalSeats()));
            }
        });

        addRoomButton.setOnAction(e -> {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                int totalSeats = Integer.parseInt(totalSeatsField.getText().trim());

                // Add room to database
                Room newRoom = new Room(roomNumber, totalSeats);
                boolean isOk = roomDao.insert(newRoom);
                if (isOk) {
                    showAlert("Success", "Room added successfully.");
                    roomListView.getItems().add(newRoom);
                } else {
                    showAlert("Error", "Failed to add room.");
                }
                // Refresh room list
                loadRooms();

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numeric values.");
            }
        });

        updateRoomButton.setOnAction(e -> {
            Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();
            if (selectedRoom == null) {
                showAlert("No Selection", "Please select a room to update.");
                return;
            }

            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());

                // Update room in database
                try {
                    selectedRoom.setRoomNumber(Integer.parseInt(roomNumberField.getText().trim()));
                    selectedRoom.setTotalSeats(Integer.parseInt(totalSeatsField.getText().trim()));

                    roomDao.update(selectedRoom);
                    showAlert("Success", "Room updated successfully.");
                    roomListView.refresh();
                    loadRooms();
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please enter valid numeric values.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numeric values.");
            }
        });

        deleteRoomButton.setOnAction(e -> {
            Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();
            if (selectedRoom == null) {
                showAlert("No Selection", "Please select a room to delete.");
                return;
            }

            // Confirm deletion
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Room");
            confirm.setContentText("Are you sure you want to delete Room " + selectedRoom.getRoomNumber() + "? This will delete all associated seats as well.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete room from database
                    roomDao.delete(selectedRoom.getId());
                    showAlert("Success", "Room deleted successfully.");
                    roomListView.refresh();
                    loadRooms();
                } else {

                }
            });
        });

        manageSeatsButton.setOnAction(e -> {
            Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();
            if (selectedRoom == null) {
                showAlert("No Selection", "Please select a room to manage its seats.");
                return;
            }

            openSeatManagementDialog(selectedRoom);
        });

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();

    }

    private void openSeatManagementDialog(Room room) {
        // Create a dialog for seat management
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Seat Management");
        dialog.setHeaderText("Manage Seats for Room " + room.getRoomNumber());

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the grid for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Seat list view with details
        TableView<Seat> seatTableView = new TableView<>();
        seatTableView.setPrefHeight(300);

        TableColumn<Seat, String> seatNumberCol = new TableColumn<>("Seat Number");
        seatNumberCol.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getSeatNumber()
                )
        );

        TableColumn<Seat, String> seatTypeCol = new TableColumn<>("Seat Type");
        seatTypeCol.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() -> {
                    SeatType type = seatTypes.get(cellData.getValue().getSeatType());
                    return type != null ? type.getName() : "Unknown";
                })
        );

        TableColumn<Seat, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().isBooked() ? "Booked" : "Available"
                )
        );

        seatTableView.getColumns().addAll(seatNumberCol, seatTypeCol, statusCol);

        // Load seats for the selected room
        loadSeatsForTable(room.getId(), seatTableView);

        // Seat details form
        TextField seatNumberField = new TextField();
        seatNumberField.setPromptText("Seat Number (e.g., A1)");

        ComboBox<SeatType> seatTypeComboBox = new ComboBox<>();
        seatTypeComboBox.setPromptText("Select Seat Type");
        ObservableList<SeatType> typeList = FXCollections.observableArrayList(seatTypes.values());
        seatTypeComboBox.setItems(typeList);

        Button addSeatButton = new Button("Add Seat");
        Button updateSeatButton = new Button("Update Seat");
        Button deleteSeatButton = new Button("Delete Seat");
        Button addMultipleButton = new Button("Add Multiple Seats");

        // Add elements to the grid
        grid.add(new Label("Seats:"), 0, 0);
        grid.add(seatTableView, 0, 1, 2, 1);

        grid.add(new Label("Seat Number:"), 0, 2);
        grid.add(seatNumberField, 1, 2);

        grid.add(new Label("Seat Type:"), 0, 3);
        grid.add(seatTypeComboBox, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addSeatButton, updateSeatButton, deleteSeatButton, addMultipleButton);
        grid.add(buttonBox, 0, 4, 2, 1);

        // Event handling
        seatTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                seatNumberField.setText(newVal.getSeatNumber());

                SeatType type = seatTypes.get(newVal.getSeatType());
                if (type != null) {
                    seatTypeComboBox.setValue(type);
                }
            }
        });

        addSeatButton.setOnAction(e -> {
            String seatNumber = seatNumberField.getText().trim();
            SeatType selectedType = seatTypeComboBox.getValue();
            Room selectedRoom = roomSelector.getValue();

            if (seatNumber.isEmpty() || !seatNumber.matches("[A-Z]\\d+")) {
                showAlert("Invalid Input", "Please enter a valid seat number (e.g., A1).");
                return;
            }

            if (selectedType == null) {
                showAlert("No Selection", "Please select a seat type.");
                return;
            }

            if (selectedRoom == null) {
                showAlert("No Room Selected", "Please select a room to add seats.");
                return;
            }

            // Add seat to database
            Seat newSeat = new Seat();
            newSeat.setSeatNumber(seatNumber);
            newSeat.setSeatType(selectedType);
            newSeat.setRoom(selectedRoom);

//            SeatDao seatDao = new SeatDao();
            boolean isOk = seatDao.insert(newSeat);
            if (isOk) {
                showAlert("Success", "Seat added successfully!");
                seatNumberField.clear();
                seatTypeComboBox.getSelectionModel().clearSelection();
                roomSelector.getSelectionModel().clearSelection();
            } else {
                showAlert("Error", "Can't insert seat into database.");
            }
        });

        updateSeatButton.setOnAction(e -> {
            Seat selectedSeat = seatTableView.getSelectionModel().getSelectedItem();
            if (selectedSeat == null) {
                showAlert("No Selection", "Please select a seat to update.");
                return;
            }

            String seatNumber = seatNumberField.getText().trim();
            SeatType selectedType = seatTypeComboBox.getValue();
            Room selectedRoom = roomSelector.getValue();

            if (seatNumber.isEmpty() || !seatNumber.matches("[A-Z]\\d+")) {
                showAlert("Invalid Input", "Please enter a valid seat number (e.g., A1).");
                return;
            }

            if (selectedType == null) {
                showAlert("No Selection", "Please select a seat type.");
                return;
            }

            if (selectedRoom == null) {
                showAlert("No Room Selected", "Please select a room to update seat.");
                return;
            }

            // Update seat in database
            selectedSeat.setSeatNumber(seatNumber);
            selectedSeat.setSeatType(selectedType);
            selectedSeat.setRoom(selectedRoom);

//            SeatDao seatDao = new SeatDao();
            seatDao.update(selectedSeat);

            showAlert("Successful", "Seat updated.");
            seatTableView.refresh();
        });

        deleteSeatButton.setOnAction(e -> {
            Seat selectedSeat = seatTableView.getSelectionModel().getSelectedItem();
            if (selectedSeat == null) {
                showAlert("No Selection", "Please select a seat to delete.");
                return;
            }

            // Confirm deletion
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Seat");
            confirm.setContentText("Are you sure you want to delete seat " + selectedSeat.getSeatNumber() + "?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete seat from database
//                    SeatDao seatDao = new SeatDao();
                    seatDao.delete(selectedSeat.getId());

                    showAlert("Successful", "Seat deleted.");
                    seatTableView.getItems().remove(selectedSeat);
                }
            });
        });


        addMultipleButton.setOnAction(e -> openAddMultipleSeatsDialog(room, seatTableView));

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(600);
        dialog.showAndWait();
    }

    private void toggleSeatSelection(Seat seat, Button seatButton) {
        if (seat.isBooked()) {
            System.out.println("Cannot select booked seat.");
            return;
        }
        if (selectedSeats.contains(seat)){
            //bo chon
            selectedSeats.remove(seat);
            seatButton.getStyleClass().remove("seat-selected");
            //khoi phuc lai style ban dau
            SeatType type = seatTypes.get(seat.getSeatType());
            if (type != null) {
                //dam bao chi 1 style dc ap dung
                seatButton.getStyleClass().removeAll(
                        "seat-normal", "seat-vip", "seat-double", "seat-booked"
                );
                seatButton.getStyleClass().add("seat-" + type.getName().toLowerCase());
            }
        }else {
            //them vao danh sach chon
            selectedSeats.add(seat);
            seatButton.getStyleClass().add("seat-selected");
        }
        updateSelectedSeatsInfo();
    }

    private void updateSelectedSeatsInfo() {
        int count = selectedSeats.size();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Seat seat: selectedSeats) {
            SeatType type = seatTypes.get(seat.getSeatType());
            if (type != null) {
                totalPrice = totalPrice.add(type.getPrice());
            }
        }
        selectedSeatsLabel.setText("Selected Seats: " + count);
        totalPriceLabel.setText(String.format("Total Price: %.2f", totalPrice));
    }

    private void updateTotalSeatsLabel() {
        Room selectedRoom = roomSelector.getValue();
        if (selectedRoom != null) {
            totalSeatsLabel.setText("Total Seats: " + selectedRoom.getTotalSeats());
        } else {
            totalSeatsLabel.setText("Total Seats: 0");
        }
    }

    private void clearSelection() {
        selectedSeats.clear();
        updateSelectedSeatsInfo();
    }

    private void processPayment() {
        if (selectedSeats.isEmpty()) {
            showAlert("No Seats Selected", "Please select at least one seat before proceeding to payment.");
            return;
        }

        // Here you would typically open a payment dialog or proceed to payment processing
        //  just show a confirmation dialog

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

    private void loadSeatsForTable(int roomId, TableView<Seat> tableView) {
        ObservableList<Seat> seats = seatDao.getSeatsByRoomId(roomId);
        if (seats != null) {
            tableView.setItems(seats);
        } else {
            showAlert("Database Error", "Failed to load seats.");
        }
    }

    private void openAddMultipleSeatsDialog(Room room, TableView<Seat> tableView) {
        //tao 1 op thoai de them nhieu ghe
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Multiple Seats");
        dialog.setHeaderText("Add Multiple Seats to Room " + room.getRoomNumber());

        //cai dat kieu button
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //tao gridlayout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //form fields
        Label rowStartLabel = new Label("Start Row (letter)");
        TextField rowStartField = new TextField("A");
        Label rowEndLabel = new Label("End Row (letter):");
        TextField rowEndField = new TextField("Z");

        Label colStartLabel = new Label("Start Column (number):");
        TextField colStartField = new TextField("1");

        Label colEndLabel = new Label("End column (number): ");
        TextField colEndField = new TextField("14");

        Label seatTypeLabel = new Label("Seat Type: ");
        ComboBox<SeatType> seatTypeComboBox = new ComboBox<>();
        seatTypeComboBox.setPromptText("Select Seat Type");
        ObservableList<SeatType> typeList = FXCollections.observableArrayList(seatTypes.values());
        seatTypeComboBox.setItems(typeList);

        // Add elements to the grid
        grid.add(rowStartLabel, 0, 0);
        grid.add(rowStartField, 1, 0);
        grid.add(rowEndLabel, 0, 1);
        grid.add(rowEndField, 1, 1);
        grid.add(colStartLabel, 0, 2);
        grid.add(colStartField, 1, 2);
        grid.add(colEndLabel, 0, 3);
        grid.add(colEndField, 1, 3);
        grid.add(seatTypeLabel, 0, 4);
        grid.add(seatTypeComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);
        //convert ket qua thanh mot phong khi dc click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    char startRow = rowStartField.getText().toUpperCase().charAt(0);
                    char endRow = rowEndField.getText().toUpperCase().charAt(0);
                    int startCol = Integer.parseInt(colStartField.getText());
                    int endCol = Integer.parseInt(colEndField.getText());
                    SeatType selectedType = seatTypeComboBox.getValue();

                    if (selectedType == null) {
                        showAlert("Error", "Please select a seat type.");
                        return null;
                    }
                    //them ghe vao database
                    int addedCount = 0;
                    List<Seat> seatsToAdd = new ArrayList<>();
                    for (char row = startRow; row <= endRow; row++) {
                        for (int col = startCol; col <= endCol; col++) {
                            String seatNumber = row + String.valueOf(col);
                            Seat seat = new Seat();
                            seat.setRoom(room);
                            seat.setSeatType(selectedType);
                            seat.setSeatNumber(seatNumber);
                            seatsToAdd.add(seat);
                        }
                    }
                    boolean success = seatDao.insertBatch(seatsToAdd);

                    if (success) {
                        showAlert("Success", seatsToAdd.size() + " seats added successfully!");
                        loadSeatsForTable(room.getId(), tableView);
                        loadRooms();
                    } else {
                        showAlert("Error", "Failed to add seats.");
                    }

                    return ButtonType.OK;
                }catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                    showAlert("Invalid Input", "Please enter valid values.");
                    return null;
                }
            }
            return ButtonType.CANCEL;
        });
        dialog.showAndWait();
    }

    @FXML
    private void onRoomSelected() {
        if (roomSelector.getValue().getRoomNumber() != null) {
            Room selectedRoom = roomSelector.getValue();
            loadSeatsForRoom(selectedRoom.getId());
            updateTotalSeatsLabel();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setShowtime(Showtime showtime) {
    }

//    @FXML
//    public void initialize() {
//        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
//        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
//            for (int colIndex = 1; colIndex <= 24; colIndex++) {
//                String seatId = rows[rowIndex] + colIndex;
//                Button seatButton = createSeatButton(seatId);
//
//                // Xác định loại ghế và gán giá từ DB
//                if (rowIndex >= 5 && rowIndex <= 7) {
//                    seatButton.getStyleClass().add("seat-vip");
//                } else if (rowIndex >= 10 && rowIndex <= 12 && colIndex % 2 == 0) {
//                    seatButton.getStyleClass().add("seat-double");
//                } else {
//                    seatButton.getStyleClass().add("seat-normal");
//                }
//
//                // Ghế đã đặt
//                if (rowIndex == 2 || rowIndex == 6) {
//                    seatButton.getStyleClass().add("seat-booked");
//                    seatButton.setDisable(true);
//                }
//
//                seatGrid.add(seatButton, colIndex - 1, rowIndex);
//            }
//        }
//    }

//    private Button createSeatButton(String seatLabel) {
//        Button button = new Button(seatLabel);
//        button.getStyleClass().add("seat-button");
//
//        button.setOnAction(event -> {
//            if (!button.getStyleClass().contains("seat-booked")) {
//                if (button.getStyleClass().contains("seat-selected")) {
//                    button.getStyleClass().remove("seat-selected");
//                    totalPrice -= seatPrices.get(getSeatType(button));
//                } else {
//                    button.getStyleClass().add("seat-selected");
//                    totalPrice += seatPrices.get(getSeatType(button));
//                }
//                updateTotalPrice();
//            }
//        });
//
//        return button;
//    }
//
//    private String getSeatType(Button button) {
//        if (button.getStyleClass().contains("seat-vip")) return "seat-vip";
//        if (button.getStyleClass().contains("seat-double")) return "seat-double";
//        return "seat-normal";
//    }
//
//    private void updateTotalPrice() {
//        totalPriceLabel.setText("Total Price: " + totalPrice + " VND");
//    }
}
