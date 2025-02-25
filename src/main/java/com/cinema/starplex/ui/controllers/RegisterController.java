package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
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


}
