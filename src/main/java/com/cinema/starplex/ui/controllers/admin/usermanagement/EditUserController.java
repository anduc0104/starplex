package com.cinema.starplex.ui.controllers.admin.usermanagement;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import com.cinema.starplex.models.UserFX;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class EditUserController {

    @FXML
    public ComboBox<String> roleComboBox;
    @FXML
    public Label roleError;

    @FXML
    public TextField phoneField;
    @FXML
    public Label phoneError;

    @FXML
    public Label passwordConfirmedError;
    @FXML
    public PasswordField passwordConfirmedField;

    @FXML
    public Label passwordError;
    @FXML
    public PasswordField passwordField;

    @FXML
    public Label emailError;
    @FXML
    public TextField emailField;

    @FXML
    public Label usernameError;
    @FXML
    public TextField usernameField;

    @FXML
    public Label fullNameError;
    @FXML
    public TextField fullNameField;

    public Integer userID;

    private UserFX user;
    private UserDao userDao;

    public void initialize() {
        userDao = new UserDao();
    }

    public void goBack(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/list-user.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot(); // Lấy Root từ FXMLLoader
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");
            mainPane.setCenter(newView); // Thay đổi nội dung của center
        } else {
            System.err.println("Failed to load addnew-user.fxml");
        }
    }

    public void setUser(UserFX user) {
        this.user = user;

        // Kiểm tra giá trị của user
        System.out.println("User data: " + user);
        System.out.println("Username: " + user.getUsername());
        System.out.println("Full name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Role: " + user.getRole());
        System.out.println("User ID: " + user.idProperty().get());

        fullNameField.setText(user.getFullName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        roleComboBox.setValue(user.getRole());
        userID = user.idProperty().get();

    }

    public void updateUser(ActionEvent event) {
        clearErrors();
        long idLong = userID.longValue();
        User existingUser = userDao.findById(idLong);

        if (existingUser == null) {
            showAlert("Error", "User not found!", Alert.AlertType.ERROR);
            return;
        }
        String username = usernameField.getText() != null ? usernameField.getText() : "";
        String fullName = fullNameField.getText() != null ? fullNameField.getText().trim() : "";
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String phone = phoneField.getText() != null ? phoneField.getText().trim() : "";
        String role = (roleComboBox.getValue() != null) ? roleComboBox.getValue() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";
        String confirmPassword = passwordConfirmedField.getText() != null ? passwordConfirmedField.getText() : "";


        boolean isValid = true;

        if (username.isEmpty() || username.length() < 4) {
            showError(usernameField, usernameError, "Username must be at least 4 characters");
            isValid = false;
        }
        if (fullName.isEmpty()) {
            showError(fullNameField, fullNameError, "Full name cannot be empty");
            isValid = false;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showError(emailField, emailError, "Invalid email format");
            isValid = false;
        }

        if (!phone.matches("^\\d{10}$")) {
            showError(phoneField, phoneError, "Phone number must be exactly 10 digits");
            isValid = false;
        }

        if (role == null || role.isEmpty()) {
            showError(roleComboBox, roleError, "Please select a role");
            isValid = false;
        }

        boolean updatePassword = !password.isEmpty();
        if (updatePassword) {
            if (password.length() < 8) {
                showError(passwordField, passwordError, "Password must be at least 8 characters");
                isValid = false;
            }
            if (!password.equals(confirmPassword)) {
                showError(passwordConfirmedField, passwordConfirmedError, "Password confirmation does not match");
                isValid = false;
            }
        }

        if (isValid) {
            try {
                existingUser.setFullName(fullName);
                existingUser.setEmail(email);
                existingUser.setPhone(phone);
                existingUser.setRole(role);
                existingUser.setUsername(username);

                if (updatePassword) {
                    existingUser.setPassword(password);
                }

                userDao.update(existingUser);
//                System.out.println(existingUser.getRole());
                showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
                goBack(event);
            } catch (Exception e) {
                showAlert("Error", "Failed to update user: " + e.getMessage(), Alert.AlertType.ERROR);
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
}
