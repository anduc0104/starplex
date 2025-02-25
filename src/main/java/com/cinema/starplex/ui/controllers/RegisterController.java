package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import com.cinema.starplex.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField; // Dùng PasswordField thay vì TextField
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private UserDao userDao = new UserDao();

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = "customer";

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "Please enter all field!");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Error", "Invalid email format!");
            return;
        }

        if (!phone.matches("^\\d{10}$")) {
            showAlert("Error", "Phone number must be contain 10 number!!");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be the least 6 character! !");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Confirm password is not match!");
            return;
        }
        boolean isRegistered = userDao.registerUser(username, password, email, phone, role);

        if (!isRegistered) {
            showAlert("Error", "Username already existed!");
        } else {
            showAlert("Message", "Register successfully!");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            emailField.clear();
            phoneField.clear();

            Utils.switchScene(usernameField, "/com/cinema/starplex/LoginView.fxml", "Login");
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
    public void switchToLogin(ActionEvent actionEvent) {
        try {
            Utils.switchScene(usernameField, "/com/cinema/starplex/LoginView.fxml", "Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
