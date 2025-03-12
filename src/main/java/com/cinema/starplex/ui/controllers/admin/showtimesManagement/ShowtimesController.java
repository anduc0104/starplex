package com.cinema.starplex.ui.controllers.admin.showtimesManagement;

import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ShowtimesController {

    @FXML
    private TableView<Showtime> showtimeTable;
    @FXML
    private TableColumn<Showtime, Number> idColumn;
    @FXML
    private TableColumn<Showtime, Timestamp> starttimeColumn;
    @FXML
    private TableColumn<Showtime, BigDecimal> priceColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button addNewButton;
    @FXML
    private TableColumn<Showtime, Void> actionColumn;

    private ShowTimeDao showtimeDAO;

    public ShowtimesController() {
        this.showtimeDAO = new ShowTimeDao();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadShowtimes();
        setupActionColumn();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        starttimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
    }

    private void setupActionColumn(){
        if (actionColumn == null) {
            actionColumn = new TableColumn<>("Action"); // Khởi tạo cột nếu chưa có
        }
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Showtime showtime = getTableView().getItems().get(getIndex());
                    handleEdit(event,showtime);
                });
                deleteButton.setOnAction(event -> {
                    Showtime showtime = getTableView().getItems().get(getIndex());
                    handleDelete(showtime);
                });
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        if (!showtimeTable.getColumns().contains(actionColumn)) {
            showtimeTable.getColumns().add(actionColumn);
        }
    }

    private void loadShowtimes() {
        List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
        showtimeTable.getItems().setAll(showtimes);
    }

    private void handleEdit(ActionEvent actionEvent, Showtime showtime) {
        try {
            EditShowtimeController.setSelectedShowtime(showtime);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/showtimesmanagement/edit-showtime.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Showtime showtime) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this movie?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (showtimeDAO.deleteShowtime(showtime.getId())) {
                    showtimeTable.getItems().remove(showtime);
                    System.out.println("Delete successfully.");
                } else {
                    System.out.println("Delete failed.");
                }
            }
        });
    }

    @FXML
    private void handleSwitchAddNew(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/showtimesmanagement/add-showtime.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
