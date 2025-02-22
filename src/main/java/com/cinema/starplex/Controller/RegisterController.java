package com.cinema.starplex.Controller;

import com.cinema.starplex.Connection.DatabaseConnection;
import com.cinema.starplex.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

    Connection conn = (Connection) new DatabaseConnection().getConn();

    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO user (username, password, email, phone) VALUES (?, sha1(?), ?, ?)";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful!");
            alert.showAndWait();
            Utils.switchTo((Stage) registerButton.getScene().getWindow(), "LoginView.fxml", 400, 300);
        }catch (SQLIntegrityConstraintViolationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists!");
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToLogin(ActionEvent actionEvent) throws IOException {
        Utils.switchTo((Stage) registerButton.getScene().getWindow(), "LoginView.fxml", 400, 300);
    }
}
