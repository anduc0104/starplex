package com.cinema.starplex.ui.controllers.admin.usermanagement;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import com.cinema.starplex.models.UserFX;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.ObservableList;
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

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.Optional;

public class ListUserController {
    @FXML
    private TableView<UserFX> tableView;
    @FXML
    private TableColumn<UserFX, Integer> idColumn;
    @FXML
    private TableColumn<UserFX, String> fullNameColumn;
    @FXML
    private TableColumn<UserFX, String> usernameColumn;
    @FXML
    private TableColumn<UserFX, String> mailColumn;
    @FXML
    private TableColumn<UserFX, String> phoneColumn;
    @FXML
    private TableColumn<UserFX, String> roleColumn;
    @FXML
    private TableColumn<UserFX, Void> actionColumn;

    private UserDao userDao;


    public void initialize() {
        userDao = new UserDao();
        configAttribute();
        addActionButtons();
        loadUserData();

    }

    private void addActionButtons() {
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
                    UserFX user = getTableView().getItems().get(getIndex());
                    setActionHandlers(actionBox, user);
                    setGraphic(actionBox);
                }
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

    private void setActionHandlers(HBox actionBox, UserFX userFX) {
        FontIcon editIcon = (FontIcon) actionBox.getChildren().get(0);
        FontIcon trashIcon = (FontIcon) actionBox.getChildren().get(1);

        editIcon.setOnMouseClicked(event -> handleEdit(userFX));
        trashIcon.setOnMouseClicked(event -> handleDelete(userFX));
    }

    private void handleEdit(UserFX userFX) {
        if (userFX == null) {
            showAlert("Error", "Please select a chair type to repair!");
            return;
        }

        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/edit-user.fxml");
        if (loader != null) {
            EditUserController controller = loader.getController();
            controller.setUser(userFX);

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) tableView.getScene().getRoot();
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

    private void handleDelete(UserFX userFX) {
        if (userFX == null) {
            showAlert("Error", "Please select a chair type to delete!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this chair type?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            userDao.delete(userFX.idProperty().longValue());
            loadUserData();
            showAlert("Success", "User has been deleted successfully");
        }
    }


    public void configAttribute() {
        // Cấu hình các cột với thuộc tính của User
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    public void loadUserData() {
        ObservableList<UserFX> observableUsers = userDao.loadUsers();
        // Gán dữ liệu vào TableView
        tableView.setItems(observableUsers);
        System.out.println(observableUsers);
    }

    public void addNewScene(ActionEvent event) throws IOException {
        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/addnew-user.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot(); // Lấy Root từ FXMLLoader
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");
            mainPane.setCenter(newView); // Thay đổi nội dung của center
        } else {
            System.err.println("Failed to load addnew-user.fxml");
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
