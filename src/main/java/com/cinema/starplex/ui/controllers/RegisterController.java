package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

public class RegisterController {

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    @FXML
    TextField emailField;
    @FXML
    TextField confirmPasswordField;
    @FXML
    TextField phoneField;
    @FXML
    Button registerButton;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Label emailError;
    @FXML
    private Label phoneError;
    @FXML
    private Label confirmPasswordError;

    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText();
        clearErrors();

        if (!validateRegister(username, password, confirmPassword, email, phone)) {
            return;
        }

        UserDao userDao = new UserDao();
        try {
            if (userDao.isUserExists(username, email)) {
                showError(usernameField, usernameError, "Username or email already exists");
                return;
            }
            User user = userDao.register(username, password, email, phone, "customer");

            if (user != null) {
                System.out.println("User registered successfully");
                switchToLogin(actionEvent);
            } else {
                showError(usernameField, usernameError, "Registration failed. Try again.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean validateRegister(String username, String password, String confirmPassword, String email, String phone) {
        boolean isValid = true;

        if (username.isEmpty()) {
            showError(usernameField, usernameError, "Username is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            showError(passwordField, passwordError, "Password is required");
            isValid = false;
        }

        if (!password.equals(confirmPassword)) {
            showError(confirmPasswordField, confirmPasswordError, "Passwords do not match");
            isValid = false;
        }

        if (email.isEmpty()) {
            showError(emailField, emailError, "Email is required");
            isValid = false;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError(emailField, emailError, "Invalid email format");
            isValid = false;
        }

        if (phone.isEmpty()) {
            showError(phoneField, phoneError, "Phone number is required");
            isValid = false;
        } else if (phone.length() < 10) {
            showError(phoneField, phoneError, "Phone must be at least 10 characters long");
            isValid = false;
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
        passwordError.setStyle("");
        emailError.setStyle("");
        phoneError.setStyle("");
    }

    public void switchToLogin(ActionEvent actionEvent) {
        try {
            SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),"LoginView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
