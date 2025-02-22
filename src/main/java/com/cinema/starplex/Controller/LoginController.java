package com.cinema.starplex.Controller;

import com.cinema.starplex.Connection.DatabaseConnection;
import com.cinema.starplex.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class LoginController {
    @FXML TextField usernameField;
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML TextField phoneField;
    @FXML Button loginButton;

    Connection conn = (Connection) new DatabaseConnection().getConn();

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String sql = "SELECT * from user WHERE username = ? AND password = sha1(?)";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Login Successfully!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Successfully!");
                Utils.switchTo((Stage) loginButton.getScene().getWindow(), "DashboardView.fxml", 1024, 768);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void switchToRegister(ActionEvent actionEvent) {
        try {
            Utils.switchTo((Stage) loginButton.getScene().getWindow(), "RegisterView.fxml", 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
