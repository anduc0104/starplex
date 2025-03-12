package com.cinema.starplex.ui.controllers.admin;

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

public class RoomControllerView implements Initializable {

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, Integer> idColumn;

    @FXML
    private TableColumn<Room, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Room, Integer> totalSeatsColumn;

    @FXML
    private TextField roomNumberField;

    @FXML
    private TextField totalSeatsField;

    @FXML
    private Button saveButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button clearButton;

    //  chart components
    @FXML
    private VBox chartContainer;

    @FXML
    private BarChart<String, Number> roomChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private ComboBox<String> chartTypeComboBox;

    @FXML
    private VBox tableContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Integer> pageSizeComboBox;

    private RoomDao roomDao;
    private ObservableList<Room> roomList;
    private Room selectedRoom;
    private TablePaginationUtility<Room> paginationUtility;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomDao = new RoomDao();
        initializeTableColumns();
        initializeChartComponents();
        initializePagination();
        loadRoomData();

        // lang nghe su kien khi chon 1 hang trong bang
        roomTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRoom = newSelection;
                populateFields(selectedRoom);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                clearFields();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterRooms(newValue);
        });
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        totalSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
    }

    private void initializeChartComponents() {
        // Set up chart type combo box
        ObservableList<String> chartTypes = FXCollections.observableArrayList(
                "Room Capacity", "Room Distribution", "Room Usage"
        );
        chartTypeComboBox.setItems(chartTypes);
        chartTypeComboBox.setValue("Room Capacity");
        chartTypeComboBox.setOnAction(event -> updateChart());

        // Set up chart axes
        xAxis.setLabel("Room Number");
        yAxis.setLabel("Total Seats");
        roomChart.setTitle("Room Capacity Overview");
        roomChart.setLegendVisible(false);
        roomChart.setAnimated(false);
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

    private void loadRoomData() {
        try {
            List<Room> rooms = roomDao.findAll();
            //chuyen dsach phong thanh ObservableList va hien thi len tablle view
            roomList = FXCollections.observableArrayList(rooms);
            roomTableView.setItems(roomList);
            updateChart();
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

    private void updateChart() {
        roomChart.getData().clear();

        String selectedChartType = chartTypeComboBox.getValue();

        if ("Room Capacity".equals(selectedChartType)) {
            createRoomCapacityChart();
        } else if ("Room Distribution".equals(selectedChartType)) {
            createRoomDistributionChart();
        } else if ("Room Usage".equals(selectedChartType)) {
            createRoomUsageChart();
        }
    }

    private void createRoomCapacityChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Seats");

        for (Room room : roomList) {
            series.getData().add(new XYChart.Data<>("Room " + room.getRoomNumber(), room.getTotalSeats()));
        }

        roomChart.setTitle("Room Capacity Overview");
        xAxis.setLabel("Room Number");
        yAxis.setLabel("Total Seats");
        roomChart.getData().add(series);

        // Apply custom styling
        applyDataPointStyles(series);
    }

    private void createRoomDistributionChart() {
        // Group rooms by capacity range
        int smallRooms = 0;  // < 50 seats
        int mediumRooms = 0; // 50-100 seats
        int largeRooms = 0;  // > 100 seats

        for (Room room : roomList) {
            int seats = room.getTotalSeats();
            if (seats < 50) {
                smallRooms++;
            } else if (seats <= 100) {
                mediumRooms++;
            } else {
                largeRooms++;
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Room Count");
        series.getData().add(new XYChart.Data<>("Small (<50)", smallRooms));
        series.getData().add(new XYChart.Data<>("Medium (50-100)", mediumRooms));
        series.getData().add(new XYChart.Data<>("Large (>100)", largeRooms));

        roomChart.setTitle("Room Size Distribution");
        xAxis.setLabel("Room Size Category");
        yAxis.setLabel("Number of Rooms");
        roomChart.getData().add(series);

        // Apply custom styling
        applyDataPointStyles(series);
    }

    private void createRoomUsageChart() {
        // For this example, we'll simulate room usage data
        // In a real application, you would fetch this from your database

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Usage Percentage");

        // Generate some random usage data (in a real app, this would come from your database)
        for (Room room : roomList) {
            // Simulate usage percentage (0-100%)
            double usagePercentage = Math.random() * 100;
            series.getData().add(new XYChart.Data<>("Room " + room.getRoomNumber(), usagePercentage));
        }

        roomChart.setTitle("Room Usage Statistics");
        xAxis.setLabel("Room Number");
        yAxis.setLabel("Usage Percentage (%)");
        roomChart.getData().add(series);

        // Apply custom styling
        applyDataPointStyles(series);
    }

    private void applyDataPointStyles(XYChart.Series<String, Number> series) {
        // Apply different colors to bars based on their values
        for (XYChart.Data<String, Number> data : series.getData()) {
            Number value = data.getYValue();

            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    String color;
                    if (value.doubleValue() < 30) {
                        color = "rgba(135, 206, 250, 0.8)"; // Light blue
                    } else if (value.doubleValue() < 70) {
                        color = "rgba(30, 144, 255, 0.8)"; // Medium blue
                    } else {
                        color = "rgba(0, 0, 139, 0.8)"; // Dark blue
                    }

                    newNode.setStyle("-fx-bar-fill: " + color + ";");

                    // them tooltip
                    Tooltip tooltip = new Tooltip(
                            data.getXValue() + ": " + String.format("%.1f", value.doubleValue())
                    );
                    Tooltip.install(newNode, tooltip);
                }
            });
        }
    }

    // hien thi du lieu phong vao cac text field
    private void populateFields(Room room) {
        roomNumberField.setText(String.valueOf(room.getRoomNumber()));
        totalSeatsField.setText(String.valueOf(room.getTotalSeats()));
    }

    private void clearFields() {
        roomNumberField.clear();
        totalSeatsField.clear();
        selectedRoom = null;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateFields()) {
            Room room = new Room();
            room.setRoomNumber(Integer.parseInt(roomNumberField.getText()));
            room.setTotalSeats(Integer.parseInt(totalSeatsField.getText()));

            try {
                boolean success = roomDao.insert(room);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room saved successfully");
                    loadRoomData();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save room");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
        if (selectedRoom != null && validateFields()) {
            selectedRoom.setRoomNumber(Integer.parseInt(roomNumberField.getText()));
            selectedRoom.setTotalSeats(Integer.parseInt(totalSeatsField.getText()));

            try {
                roomDao.update(selectedRoom);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully");
                loadRoomData();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    roomDao.delete(selectedRoom);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully");
                    loadRoomData();
                    clearFields();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleClearButton(ActionEvent event) {
        clearFields();
        roomTableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handlePrintChart(ActionEvent event) {
        // Implement chart printing functionality
        showAlert(Alert.AlertType.INFORMATION, "Print", "Chart printing functionality will be implemented here");
    }

    @FXML
    private void handleExportChart(ActionEvent event) {
        // Implement chart export functionality
        showAlert(Alert.AlertType.INFORMATION, "Export", "Chart export functionality will be implemented here");
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}