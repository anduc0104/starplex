package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.UserDao;

import com.cinema.starplex.models.User;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {
    @FXML
    TextField usernameField;
    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField phoneField;
    @FXML
    Button loginButton;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;

    public void handleLogin(ActionEvent actionEvent) throws SQLException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Xóa thông báo lỗi cũ
        clearErrors();

        if (!validateFields(username, password)) {
            return;
        }
        UserDao userDao = new UserDao();
        User user = userDao.login(username, password);
        if (user != null) {
            if (user.getRole().equals("admin")) {
                System.out.println("login successful");
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                SceneSwitcher.switchTo(currentStage, "admin/main-layout.fxml");
            } else {
                System.out.println("login successful");
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                SceneSwitcher.switchTo(currentStage, "staff/main-layout.fxml");
            }
        } else {
            System.out.println("login failed");
            showError(usernameField, usernameError, "Invalid username or password");
            showError(passwordField, passwordError, "Invalid username or password");
        }
    }

    public boolean validateFields(String username, String password) {
        boolean isValid = true;


        if (username.isEmpty()) {
            showError(usernameField, usernameError, "username must not leave empty");
            isValid = false;
        }
        if (password.isEmpty()) {
            showError(passwordField, passwordError, "password must not leave empty");
            isValid = false;
            return isValid;
        }
        if (password.length() < 8) {
            showError(passwordField, passwordError, "at least 8 characters");
            isValid = false;
            return isValid;
        }


        return isValid;
    }

    public void showError(TextField field, Label errorLabel, String message) {
        field.setStyle("-fx-border-color: -fx-secondary-color");
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-color: -fx-secondary-color");
    }

    private void clearErrors() {
        usernameError.setText("");
        passwordError.setText("");
        usernameField.setStyle("");
        passwordField.setStyle("");
    }

}