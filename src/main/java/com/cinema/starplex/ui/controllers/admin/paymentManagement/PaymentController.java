package com.cinema.starplex.ui.controllers.admin.paymentManagement;

import com.cinema.starplex.dao.PaymentDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Payment;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.ui.controllers.admin.seatManagement.EditSeatController;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.List;
import java.util.Optional;

public class PaymentController {
    public TextField searchField;
    @FXML
    private TableView<Payment> paymentTable;
    @FXML
    private TableColumn<Payment, Integer> colId;
    @FXML
    private TableColumn<Payment, String> colBookingId;
    @FXML
    private TableColumn<Payment, BigDecimal> colAmount;
    @FXML
    private TableColumn<Payment, String> colPaymentMethod;
    @FXML
    private TableColumn<Payment, String> colStatus;
    @FXML
    private TableColumn<Payment, String> colTransactionId;
    @FXML
    private TableColumn<Payment, Void> colAction;

    private final PaymentDao paymentDao = new PaymentDao();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookingId.setCellValueFactory(celData -> {
            Booking booking = celData.getValue().getBooking();
            return new SimpleStringProperty(booking != null ? String.valueOf(booking.getUser().getUsername()) : "N/A");
        });
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTransactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        addActionButtons();
        loadsPayment();
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
                    Payment payment = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, payment);
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setActionHandlers(HBox actionBox,Payment payment) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon deleteIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(payment));
        deleteIcon.setOnMouseClicked(event -> handleDelete(payment));
    }

    private void loadsPayment() {
        List<Payment> payments = paymentDao.findAll();
        paymentTable.setItems(FXCollections.observableArrayList(payments));
    }

    @FXML
    private void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = SceneSwitcher.loadView("admin/paymentmanagement/add-payment.fxml");
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
            System.err.println("Could not load add-payment.fxml");
        }
    }

    private void handleEdit(Payment payment) {
        if (payment == null) {
            showAlert("Error", "Please select a payment to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/paymentmanagement/edit-payment.fxml");
        if (loader != null) {
            PaymentEditController controller = loader.getController();
            controller.setPayment(payment); // Truyền dữ liệu ghế cần chỉnh sửa

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) paymentTable.getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load edit-payment.fxml");
        }
    }

    private void handleDelete(Payment payment) {
        if (payment == null) {
            showAlert("Error", "Please select a payment to delete!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this payment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            paymentDao.delete(payment.getId());
            loadsPayment();
            showAlert("Success", "Delete payment successfully!");
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
