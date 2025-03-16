package com.cinema.starplex.ui.controllers.admin.showtimesManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ShowtimesController {

    @FXML
    private TableView<Showtime> showtimeTable;
    @FXML
    private TableColumn<Showtime, Number> idColumn;
    @FXML
    private TableColumn<Showtime, String> colMovieId;
    @FXML
    private TableColumn<Showtime, String> colRoomId;
    @FXML
    private TableColumn<Showtime, LocalDate> showDateColumn; // Ngày chiếu
    @FXML
    private TableColumn<Showtime, LocalTime> showTimeColumn; // Giờ chiếu
    @FXML
    private TableColumn<Showtime, BigDecimal> priceColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button addNewButton;
    @FXML
    private TableColumn<Showtime, Void> actionColumn;

    private ShowTimeDao showtimeDAO;
    private final RoomDao roomDao = new RoomDao();
    private final MovieDao movieDao = new MovieDao();

    public ShowtimesController() {
        this.showtimeDAO = new ShowTimeDao();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
        loadShowtimes();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        showDateColumn.setCellValueFactory(cellData -> {
            java.sql.Date sqlDate = cellData.getValue().getShowDate(); // Lấy ngày từ Showtime
            LocalDate localDate = sqlDate.toLocalDate(); // Chuyển đổi trực tiếp
            return new SimpleObjectProperty<>(localDate);
        });
        showTimeColumn.setCellValueFactory(cellData -> {
            java.sql.Time sqlTime = cellData.getValue().getShowTime(); // Lấy giờ từ Showtime
            LocalTime localTime = sqlTime.toLocalTime(); // Chuyển đổi trực tiếp
            return new SimpleObjectProperty<>(localTime);
        });
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

        colRoomId.setCellValueFactory(cellData -> {
            Room room = cellData.getValue().getRoom();
            return new SimpleStringProperty(room != null ? String.valueOf(room.getRoomNumber()) : "N/A");
        });

        colMovieId.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue().getMovie();
            return new SimpleStringProperty(movie != null ? movie.getTitle() : "N/A");
        });
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
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
                    Showtime showtime = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, showtime);
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setActionHandlers(HBox actionBox, Showtime showtime) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon trashIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(showtime));
        trashIcon.setOnMouseClicked(event -> handleDelete(showtime));
    }

    private void loadShowtimes() {
        List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
        showtimeTable.getItems().setAll(showtimes);
    }

    private void handleEdit(Showtime showtime) {
        if (showtime == null) {
            showAlert("Error", "Please select a showtime to edit!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/showtimesmanagement/edit-showtime.fxml");
        if (loader != null) {
            EditShowtimeController controller = loader.getController();
            controller.setSelectedShowtime(showtime);

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) showtimeTable.getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load edit-showtime.fxml");
        }
    }

    private void handleDelete(Showtime showtime) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this showtime?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (showtimeDAO.deleteShowtime(showtime.getId())) {
                    showtimeTable.getItems().remove(showtime);
                    showAlert("Success", "Showtime deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete showtime.");
                }
            }
        });
    }

    public void handleAdd(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/showtimesmanagement/add-showtime.fxml");
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
            System.err.println("Could not load add-showtime.fxml");
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