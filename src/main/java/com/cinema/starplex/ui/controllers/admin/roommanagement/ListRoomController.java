package com.cinema.starplex.ui.controllers.admin.roommanagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.ui.functions.TablePaginationUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListRoomController implements Initializable {

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, Integer> idColumn;

    @FXML
    private TableColumn<Room, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Room, Integer> totalSeatsColumn;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Integer> pageSizeComboBox;
    @FXML
    private VBox tableContainer;

    private RoomDao roomDao;
    private ObservableList<Room> roomList;
    private Room selectedRoom;
    private TablePaginationUtility<Room> paginationUtility;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomDao = new RoomDao();
        initializeTableColumns();
        initializePagination();
        loadRoomData();

        // lang nghe su kien khi chon 1 hang trong bang
        roomTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRoom = newSelection;
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterRooms(newValue);
        });
    }

    private void loadRoomData() {
        try {
            List<Room> rooms = roomDao.findAll();
            //chuyen dsach phong thanh ObservableList va hien thi len tablle view
            roomList = FXCollections.observableArrayList(rooms);
            roomTableView.setItems(roomList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterRooms(String searchText) {
        if (roomList == null) {
            return;
        }

        if (searchText == null || searchText.isEmpty()) {
            //neu la empty thi show tat ca room
            paginationUtility.setItems(roomList);
        } else {
            //tim kiem dua vao searchText
            List<Room> filteredList = roomList.stream()
                    .filter(room ->
                            String.valueOf(room.getId()).contains(searchText) || String.valueOf(room.getRoomNumber()).contains(searchText) || String.valueOf(room.getTotalSeats()).contains(searchText)
                    ).collect(Collectors.toList());
            paginationUtility.setItems(filteredList);
        }
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        totalSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
    }


    private void initializePagination() {
        //tao pagination with 10 rows cho moi page khoi tao
        paginationUtility = new TablePaginationUtility<>(roomTableView, 10);

        //thay the table view trong container voi pagination container
        tableContainer.getChildren().clear();
        tableContainer.getChildren().add(paginationUtility.getContainer());

        //hoi tao page voi size options
        ObservableList<Integer> pageSizeOptions = FXCollections.observableArrayList(5, 10, 20, 50, 100);
        pageSizeComboBox.setItems(pageSizeOptions);
        pageSizeComboBox.setValue(10);
        pageSizeComboBox.setOnAction(event -> {
            //tao mot pagination utility with 10 rows cho moi page khoi tao
            int selectedPageSize = pageSizeComboBox.getValue();
            paginationUtility = new TablePaginationUtility<>(roomTableView, selectedPageSize);

            //thayy the container
            tableContainer.getChildren().clear();
            tableContainer.getChildren().add(paginationUtility.getContainer());

            //update voi du lieu hien tai
            if (roomList != null) {
                paginationUtility.setItems(roomList);
            }
        });
    }


    @FXML
    private void handleDeleteButton(ActionEvent event) {
        if (selectedRoom != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Room");
            confirmAlert.setContentText("Are you sure you want to delete this room?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    roomDao.delete(selectedRoom.getId().longValue());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully");
                    loadRoomData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void handleAdd(ActionEvent event) {

    }
}