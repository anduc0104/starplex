package com.cinema.starplex.ui.controllers.admin.roommanagement;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.ui.functions.TablePaginationUtility;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListRoomController implements Initializable {
    @FXML
    public TableColumn<Room, Void> colAction;
    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, Integer> idColumn;

    @FXML
    private TableColumn<Room, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Room, Integer> totalSeatsColumn;

    //    @FXML
//    private TextField searchField;
//    @FXML
//    private ComboBox<Integer> pageSizeComboBox;
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
//        initializePagination();
        loadRoomData();
        addActionButtons();

        // Set up search functionality
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filterRooms(newValue);
//        });
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
                    Room room = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, room);
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setActionHandlers(HBox actionBox, Room room) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon deleteIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(room));
        deleteIcon.setOnMouseClicked(event -> handleDelete(room));
    }

    private void handleEdit(Room room) {
        if (room == null) {
            showAlert("Error", "Please select a room to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/roommanagement/edit-room.fxml");
        if (loader != null) {
            EditRoomController controller = loader.getController();
            controller.setRoom(room);

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) roomTableView.getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load edit-seat-type.fxml");
        }
    }

    public void handleAdd(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/roommanagement/addnew-room.fxml");
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
//    private void filterRooms(String searchText) {
//        if (roomList == null) {
//            return;
//        }
//
//        if (searchText == null || searchText.isEmpty()) {
//            //neu la empty thi show tat ca room
//            paginationUtility.setItems(roomList);
//        } else {
//            //tim kiem dua vao searchText
//            List<Room> filteredList = roomList.stream()
//                    .filter(room ->
//                            String.valueOf(room.getId()).contains(searchText) || String.valueOf(room.getRoomNumber()).contains(searchText) || String.valueOf(room.getTotalSeats()).contains(searchText)
//                    ).collect(Collectors.toList());
//            paginationUtility.setItems(filteredList);
//        }
//    }
    private void initializeTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        totalSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
    }
    //    private void initializePagination() {
//        //tao pagination with 10 rows cho moi page khoi tao
//        paginationUtility = new TablePaginationUtility<>(roomTableView, 10);
//
//        //thay the table view trong container voi pagination container
//        tableContainer.getChildren().clear();
//        tableContainer.getChildren().add(paginationUtility.getContainer());
//
//        //hoi tao page voi size options
//        ObservableList<Integer> pageSizeOptions = FXCollections.observableArrayList(5, 10, 20, 50, 100);
//        pageSizeComboBox.setItems(pageSizeOptions);
//        pageSizeComboBox.setValue(10);
//        pageSizeComboBox.setOnAction(event -> {
//            //tao mot pagination utility with 10 rows cho moi page khoi tao
//            int selectedPageSize = pageSizeComboBox.getValue();
//            paginationUtility = new TablePaginationUtility<>(roomTableView, selectedPageSize);
//
//            //thayy the container
//            tableContainer.getChildren().clear();
//            tableContainer.getChildren().add(paginationUtility.getContainer());
//
//            //update voi du lieu hien tai
//            if (roomList != null) {
//                paginationUtility.setItems(roomList);
//            }
//        });
//    }
    @FXML
    private void handleDelete(Room room) {
        if (room == null) {
            showAlert("Error", "Please select a chair type to delete!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this chair type?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            roomDao.delete(room.getId());
            loadRoomData();
            showAlert("Success", "User has been deleted successfully");
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