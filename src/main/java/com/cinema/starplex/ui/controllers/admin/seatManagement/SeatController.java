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
import javafx.scene.layout.*;
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
    public TextField searchField;
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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}