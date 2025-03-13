package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddGenreController {

    @FXML
    private TextField nameField;
    @FXML
    private Button saveButton, clearButton;

    private ObservableList<String> genreList = FXCollections.observableArrayList();


    @FXML
    private void handleSave() {
        String genreName = nameField.getText();

        if (genreName.isEmpty()) {
            showAlert("Error", "Please fill in all required fields!", Alert.AlertType.ERROR);
            return;
        }

        if (genreList.contains(genreName)) {
            showAlert("Error", "Genre already exists\n", Alert.AlertType.ERROR);
            return;
        }

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO movie_genres (name) VALUES (?)")) {

            stmt.setString(1, genreName);
            stmt.executeUpdate();
            showAlert("Success", "Genre added Successfully", Alert.AlertType.INFORMATION);
            handleClear();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add genre.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        returnToGenreView(actionEvent);
    }

    private void returnToGenreView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/moviegenremanagement/genre-view.fxml"));
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

}