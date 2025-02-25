package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import com.cinema.starplex.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


public class LoginController {
    @FXML TextField usernameField;
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML TextField phoneField;
    @FXML Button loginButton;

    private UserDao userDao = new UserDao();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password!");
            return;
        }

        User user = userDao.getUserByUsername(username);

        if (user == null) {
            showAlert("Error", "Invalid Username!");
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            showAlert("Error", "Invalid password!");
        } else {
            showAlert("Success", "Login Successfull!");
            usernameField.clear();
            passwordField.clear();
            //chuyen sang giao dien cua dashboard
            Utils.switchScene(usernameField, "/com/cinema/starplex/Dashboard-View.fxml", "Dashboard");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void switchToRegister(ActionEvent actionEvent) {
        try {
            Utils.switchScene(usernameField, "/com/cinema/starplex/RegisterView.fxml", "Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
