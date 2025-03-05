package com.cinema.starplex.ui.controllers.admin.seatManagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.SeatTypeDao;
import com.cinema.starplex.dao.SeatDao;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.geometry.Pos;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class SeatController {
    @FXML
    private ComboBox<Room> roomComboBox; // ComboBox cho Room
    @FXML
    private ComboBox<SeatType> seatTypeComboBox; // ComboBox cho SeatType
    @FXML
    private TableView<Seat> seatTable;
    @FXML
    private TableColumn<Seat, Integer> colId;
    @FXML
    private TableColumn<Seat, String> colRoomId;
    @FXML
    private TableColumn<Seat, String> colSeatTypeId;
    @FXML
    private TableColumn<Seat, String> colSeatNumber;
    @FXML
    private TableColumn<Seat, Void> colAction;

    private final SeatDao seatDao = new SeatDao();
    private final RoomDao roomDao = new RoomDao(); // Khởi tạo RoomDao
    private final SeatTypeDao seatTypeDao = new SeatTypeDao(); // Khởi tạo SeatTypeDao

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRoomId.setCellValueFactory(cellData -> {
            Room room = cellData.getValue().getRoom();
            return new SimpleStringProperty(room != null ? String.valueOf(room.getRoomNumber()) : "N/A"); // Kiểm tra null
        });

        colSeatTypeId.setCellValueFactory(cellData -> {
            SeatType seatType = cellData.getValue().getSeatType();
            return new SimpleStringProperty(seatType != null ? seatType.getName() : "N/A"); // Kiểm tra null
        });
        colSeatNumber.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        addActionButtons();
        loadSeats();
        loadRooms(); // Tải danh sách phòng
        loadSeatTypes(); // Tải danh sách loại ghế
    }

    private void loadRooms() {
        // Danh sách Room
        ObservableList<Room> rooms = FXCollections.observableArrayList(roomDao.findAll()); // Tải dữ liệu từ RoomDao
        roomComboBox.setItems(rooms);

        // Đặt StringConverter để hiển thị room_number
        roomComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                return room != null ? String.valueOf(room.getRoomNumber()) : ""; // Hiển thị room_number
            }

            @Override
            public Room fromString(String string) {
                // Xử lý chuyển đổi từ String về Room nếu cần
                return roomDao.findByNumber(Integer.parseInt(string));
            }
        });
    }

    private void loadSeatTypes() {
        // Danh sách SeatType
        ObservableList<SeatType> seatTypes = FXCollections.observableArrayList(seatTypeDao.findAll()); // Tải dữ liệu từ SeatTypeDao
        seatTypeComboBox.setItems(seatTypes);

        // Đặt StringConverter để hiển thị name
        seatTypeComboBox.setConverter(new StringConverter<SeatType>() {
            @Override
            public String toString(SeatType seatType) {
                return seatType != null ? seatType.getName() : ""; // Hiển thị name
            }

            @Override
            public SeatType fromString(String string) {
                // Xử lý chuyển đổi từ String về SeatType nếu cần
                return seatTypeDao.findByName(string);
            }
        });
    }

    private void addActionButtons() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final HBox actionBox = new HBox(10);

            private final FontIcon editIcon = new FontIcon(FontAwesomeSolid.EDIT);
            private final FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);

            {
                // Đặt kích thước và màu sắc cho các biểu tượng
                editIcon.setIconSize(20);
                editIcon.setIconColor(Paint.valueOf("#4CAF50")); // Màu xanh lá cho "Sửa"

                deleteIcon.setIconSize(20);
                deleteIcon.setIconColor(Paint.valueOf("#F44336")); // Màu đỏ cho "Xóa"

                actionBox.getChildren().addAll(editIcon, deleteIcon);
                actionBox.setAlignment(Pos.CENTER); // Căn giữa HBox
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Seat seat = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, seat);
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setActionHandlers(HBox actionBox, Seat seat) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon deleteIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(seat));
        deleteIcon.setOnMouseClicked(event -> handleDelete(seat));
    }

    private void loadSeats() {
        List<Seat> seats = seatDao.findAll();
        seatTable.setItems(FXCollections.observableArrayList(seats));
    }

    @FXML
    private void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = SceneSwitcher.loadView("admin/seatmanagement/add-seat.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load add-seat.fxml");
        }
    }

    private void handleEdit(Seat seat) {
        if (seat == null) {
            showAlert("Error", "Please select a seat to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/seatmanagement/edit-seat.fxml");
        if (loader != null) {
            EditSeatController controller = loader.getController();
            controller.setSeat(seat); // Truyền dữ liệu ghế cần chỉnh sửa

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) seatTable.getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load edit-seat.fxml");
        }
    }

    private void handleDelete(Seat seat) {
        if (seat == null) {
            showAlert("Error", "Please select a seat to delete!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this seat?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            seatDao.delete(seat.getId());
            loadSeats();
            showAlert("Success", "Delete seat successfully!");
        }
    }

    private void showSeatDialog(Seat seat) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(seat == null ? "Add seat" : "Edit seat");
        dialog.setHeaderText(null);

        TextField seatNumberField = new TextField(seat == null ? "" : seat.getSeatNumber());

        if (seat != null) {
            roomComboBox.setValue(seat.getRoom());
            seatTypeComboBox.setValue(seat.getSeatType());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Room:"), 0, 0);
        grid.add(roomComboBox, 1, 0);
        grid.add(new Label("Seat Type:"), 0, 1);
        grid.add(seatTypeComboBox, 1, 1);
        grid.add(new Label("Seat Number:"), 0, 2);
        grid.add(seatNumberField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Room selectedRoom = roomComboBox.getValue();
                    SeatType selectedSeatType = seatTypeComboBox.getValue();
                    String seatNumber = seatNumberField.getText();

                    if (seat == null) {
                        seatDao.save(new Seat(null, selectedRoom, selectedSeatType, seatNumber, new Timestamp(System.currentTimeMillis())));
                    } else {
                        seat.setRoom(selectedRoom);
                        seat.setSeatType(selectedSeatType);
                        seat.setSeatNumber(seatNumber);
                        seatDao.update(seat);
                    }
                    loadSeats();
                } catch (Exception e) {
                    showAlert("Error", "Please select a valid room and seat type!");
                }
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