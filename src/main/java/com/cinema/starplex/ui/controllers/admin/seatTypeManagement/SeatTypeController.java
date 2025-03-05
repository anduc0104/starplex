package com.cinema.starplex.ui.controllers.admin.seatTypeManagement;

import com.cinema.starplex.dao.SeatTypeDao;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.SceneSwitcher;
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
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SeatTypeController {
    @FXML
    private TableView<SeatType> seatTypeTable;
    @FXML
    private TableColumn<SeatType, Integer> colId;
    @FXML
    private TableColumn<SeatType, String> colName;
    @FXML
    private TableColumn<SeatType, BigDecimal> colPrice;
    @FXML
    private TableColumn<SeatType, Void> colAction;

    private final SeatTypeDao seatTypeDao = new SeatTypeDao();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        addActionButtons();
        loadSeatTypes();
    }

    private void addActionButtons() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SeatType, Void> call(final TableColumn<SeatType, Void> param) {
                return new TableCell<>() {
                    private final HBox actionBox = createActionButtons();

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            SeatType seatType = getTableView().getItems().get(getIndex());
                            setActionHandlers(actionBox, seatType);
                            setGraphic(actionBox);
                        }
                    }
                };
            }
        });
    }

    private HBox createActionButtons() {
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        FontIcon editIcon = new FontIcon(FontAwesomeSolid.EDIT);
        FontIcon trashIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);

        editIcon.setIconSize(20);
        editIcon.setIconColor(Paint.valueOf("#4CAF50")); // Màu xanh lá cho "Sửa"
        trashIcon.setIconSize(20);
        trashIcon.setIconColor(Paint.valueOf("#F44336")); // Màu đỏ cho "Xóa"

        hBox.getChildren().addAll(editIcon, trashIcon);
        return hBox;
    }

    private void setActionHandlers(HBox actionBox, SeatType seatType) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon trashIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(seatType));
        trashIcon.setOnMouseClicked(event -> handleDelete(seatType));
    }

    private void loadSeatTypes() {
        List<SeatType> seatTypes = seatTypeDao.findAll();
        seatTypeTable.setItems(FXCollections.observableArrayList(seatTypes));
    }

    @FXML
    private void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = SceneSwitcher.loadView("admin/seattypemanagement/add-seat-type.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot(); // Lấy Root từ FXMLLoader
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView); // Thay đổi nội dung của phần trung tâm
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load add-seat-type.fxml");
        }
    }

    private void handleEdit(SeatType seatType) {
        if (seatType == null) {
            showAlert("Error", "Please select a chair type to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/seattypemanagement/edit-seat-type.fxml");
        if (loader != null) {
            EditSeatTypeController controller = loader.getController();
            controller.setSeatType(seatType);

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) seatTypeTable.getScene().getRoot();
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

    private void handleDelete(SeatType seatType) {
        if (seatType == null) {
            showAlert("Error", "Please select a chair type to delete!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this chair type?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            seatTypeDao.delete(seatType.getId());
            loadSeatTypes();
        }
    }

    private void showSeatTypeDialog(SeatType seatType) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(seatType == null ? "Add seat type" : "Edit seat type");
        dialog.setHeaderText(null);

        Label nameLabel = new Label("Chair type name:");
        TextField nameField = new TextField(seatType == null ? "" : seatType.getName());

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField(seatType == null ? "" : seatType.getPrice().toString());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String name = nameField.getText().trim();
                String priceText = priceField.getText().trim();

                if (name.isEmpty() || priceText.isEmpty()) {
                    showAlert("Error", "Please enter complete information!");
                    return;
                }

                try {
                    BigDecimal price = new BigDecimal(priceText);
                    if (seatType == null) {
                        seatTypeDao.save(new SeatType(null, name, price, null));
                    } else {
                        seatType.setName(name);
                        seatType.setPrice(price);
                        seatTypeDao.update(seatType);
                    }
                    loadSeatTypes();
                } catch (NumberFormatException e) {
                    showAlert("Error", "Price must be a valid number!");
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