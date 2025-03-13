package com.cinema.starplex.ui.controllers.admin.usermanagement;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class AddnewUserController {
    @FXML
    public Label roleError;
    @FXML
    public Label phoneError;
    @FXML
    public Label passwordConfirmedError;
    @FXML
    public Label passwordError;
    @FXML
    public Label emailError;
    @FXML
    public Label usernameError;
    @FXML
    public Label fullNameError;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordConfirmedField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button backButton;

    private UserDao userDao;

    public void initialize() {
        userDao = new UserDao();
    }

    private void returnToUserView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/usermanagement/list-user.fxml"));
            Parent seatView = loader.load();

            AnchorPane root = (AnchorPane) ((Button) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) root.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(seatView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent event) {
        returnToUserView(event);
    }

    public void addUser(ActionEvent event) {
        clearErrors();
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = passwordConfirmedField.getText();
        String phone = phoneField.getText().trim();
        String role = roleComboBox.getValue();

        boolean isValid = true;

        if (fullName.isEmpty()) {
            showError(fullNameField, fullNameError, "Full name cannot be empty");
            isValid = false;
        }

        // Validate username
        if (username.isEmpty() || username.length() < 4) {
            showError(usernameField, usernameError, "Username must be at least 4 characters");
            isValid = false;
        } else if (userDao.isUsernameExists(username)) { // Kiểm tra username tồn tại
            showError(usernameField, usernameError, "Username already exists");
            isValid = false;
        }

        // Validate email format
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showError(emailField, emailError, "Invalid email format");
            isValid = false;
        }

        // Validate password length
        if (password.length() < 8) {
            showError(passwordField, passwordError, "Password must be at least 8 characters");
            isValid = false;
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            showError(passwordConfirmedField, passwordConfirmedError, "Password confirmation does not match");
            isValid = false;
        }

        // Validate phone number (must contain exactly 10 digits)
        if (!phone.matches("^\\d{10}$")) {
            showError(phoneField, phoneError, "Phone number must be exactly 10 digits");
            isValid = false;
        }

        // Validate role selection
        if (role == null || role.isEmpty()) {
            showError(roleComboBox, roleError, "Please select a role");
            isValid = false;
        }

        if (isValid) {
            try {
                userDao.save(new User(fullName, username, email, password, phone, role));
                System.out.println("User added successfully!");

                fullNameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();
                passwordConfirmedField.clear();
                phoneField.clear();
                roleComboBox.setValue(null);

                showAlert("Success", "User has been added successfully!", Alert.AlertType.INFORMATION);
                returnToUserView(event);  // Chuyển về trang danh sách user
            } catch (Exception e) {  // Bắt lỗi nếu có vấn đề khi lưu user
                showAlert("Error", "Failed to add user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void showError(Control field, Label errorLabel, String message) {
        if (field instanceof TextField || field instanceof PasswordField) {
            field.setStyle("-fx-border-color: red");
        } else if (field instanceof ComboBox) {
            field.setStyle("-fx-border-color: red");
        }
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red");
    }


    private void clearErrors() {
        usernameError.setText("");
        usernameField.setStyle("");

        passwordError.setText("");
        passwordField.setStyle("");

        passwordConfirmedError.setText("");
        passwordConfirmedField.setStyle("");

        roleError.setText("");
        roleComboBox.setStyle("");

        emailError.setText("");
        emailField.setStyle("");

        fullNameError.setText("");
        fullNameField.setStyle("");

        phoneError.setText("");
        phoneField.setStyle("");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleClear(ActionEvent event) {
        fullNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        passwordConfirmedField.clear();
        phoneField.clear();
        roleComboBox.setValue(null);
//        xóa thng báo lỗi
        clearErrors();
    }
}
