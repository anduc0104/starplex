package com.cinema.starplex.ui.controllers.admin.usermanagement;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.UserFX;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

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
        setupActionColumn();
        loadUserData();

    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    UserFX user = getTableView().getItems().get(getIndex());
                    handleEdit(user, event);
                });

                deleteButton.setOnAction(event -> {
                    UserFX user = getTableView().getItems().get(getIndex());
                    handleDelete(user);
                });
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
    }


    private void handleEdit(UserFX user, ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/edit-user.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot(); // Lấy Root từ FXMLLoader

            EditUserController controller = loader.getController();
            if (controller != null) {
                controller.setUser(user); // Truyền dữ liệu vào EditUserController
            }

            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");
            mainPane.setCenter(newView); // Thay đổi nội dung của center
        } else {
            System.err.println("Failed to load edit-user.fxml");
        }
    }

    private void handleDelete(UserFX user) {
        try {
            System.out.println("Delete user: " + user.idProperty().get());
            userDao.deleteUserFX(user);
            loadUserData();
            showAlert("Success", "User has been deleted successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        // Hiển thị hộp thoại xác nhận và xóa User từ database
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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
