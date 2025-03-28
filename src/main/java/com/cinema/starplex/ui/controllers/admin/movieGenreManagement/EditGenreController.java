package com.cinema.starplex.ui.controllers.admin.movieGenreManagement;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditGenreController {
    @FXML
    private TextField nameField;
    @FXML
    private Button saveButton, clearButton;

    private Genre genre;

    public void setGenre(Genre genre) {
        this.genre = genre;
        nameField.setText(genre.getName());
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) throws SQLException {
        String sql = "UPDATE movie_genres SET name = ? WHERE id = ?";
        try (Connection conn =new DatabaseConnection().getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setInt(2, genre.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                genre.setName(nameField.getText());

                handleClear(actionEvent);
                showAlert("Success", "Genre edited Successfully", Alert.AlertType.INFORMATION);
                returnToGenreView(actionEvent);
            }
        }
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        nameField.clear();
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
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}