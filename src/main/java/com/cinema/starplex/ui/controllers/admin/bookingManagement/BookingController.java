package com.cinema.starplex.ui.controllers.admin.bookingManagement;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.models.User;
import com.cinema.starplex.util.AlertUtils;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private TableView<Booking> bookingTableView;

    @FXML
    private TableColumn<Booking, Integer> idColumn;


    @FXML
    private TableColumn<Booking, String> userColumn;

    @FXML
    private TableColumn<Booking, String> showtimeColumn;

    @FXML
    private TableColumn<Booking, BigDecimal> totalPriceColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private TableColumn<Booking, Timestamp> createdAtColumn;

    @FXML
    private TableColumn<Booking, Void> colAction;

    private BookingDao bookingDao;
    private UserDao userDao;
    private ShowTimeDao showtimeDao;
    private ObservableList<Booking> bookingList;
//    private final String[] STATUSES = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookingDao = new BookingDao();
        userDao = new UserDao();
        showtimeDao = new ShowTimeDao();

        // Khởi tạo cột trong bảng
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        addActionButtons();
        loadUsers();
        loadShowtimes();
        loadBookingData(); // Load danh sách booking cho bảng
        for (Booking booking : bookingList) {
            System.out.println("Booking ID: " + booking.getId() +
                    ", Showtime: " + (booking.getShowtime() != null ? booking.getShowtime().getId() : "NULL"));
        }

    }

    private void loadUsers() {
        // Gán dữ liệu cho cột userColumn trong bảng
        userColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> {
                            User user = cellData.getValue().getUser();
                            return (user != null) ? user.getUsername() : "Unknown";
                        },
                        cellData.getValue().userProperty()));
    }

    private void loadShowtimes() {
        // Gán dữ liệu cho cột showtimeColumn trong bảng
        showtimeColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> {
                            Showtime showtime = cellData.getValue().getShowtime();
                            System.out.println("Booking ID: " + cellData.getValue().getId() +
                                    ", Showtime: " + cellData.getValue().getShowtime());
                            return (showtime != null) ? showtime.getDisplayName() : "No/A";
                        },
                        cellData.getValue().showtimeProperty()));
    }

    private void loadBookingData() {
        List<Booking> bookings = bookingDao.findAll();
        bookingList = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingList);
    }

    @FXML
    private void handleDeleteAction(Booking booking) {
        try {
            if (booking == null) {
                AlertUtils.showError("Error", "Please select a booking to delete!");
                return;
            }
            AlertUtils.showConfirmation("Yes/No", "Are you sure you want to delete this booking?");
            if (AlertUtils.getResult()) {
                bookingDao.delete(booking.getId());
                bookingList.remove(booking);
            }else {
                // Do nothing if the user cancels the deletion
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEdit(Booking booking) {
        if (booking == null) {
            AlertUtils.showError( "Error", "Please select a booking to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/booking-view/booking-edit-view.fxml");
        if (loader != null) {
            BookingEditController controller = loader.getController();
            controller.setBooking(booking); // Pass the booking to edit to the controller

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) bookingTableView.getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load edit-booking.fxml");
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = SceneSwitcher.loadView("admin/booking-view/booking-add-view.fxml");
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
            System.err.println("Could not load add-booking.fxml");
        }
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
                    Booking booking = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, booking);
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setActionHandlers(HBox actionBox, Booking booking) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon deleteIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(booking));
        deleteIcon.setOnMouseClicked(event -> handleDeleteAction(booking));
    }
}
