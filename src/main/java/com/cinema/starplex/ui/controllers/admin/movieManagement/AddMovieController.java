package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddMovieController {

    @FXML private TextField titleField;
    @FXML private TextField directorField;
    @FXML private TextField actorsField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private DatePicker releaseDatePicker;
    @FXML private TextArea descriptionField;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private ImageView movieImageView;
    @FXML private Button addImageButton;

    private String imagePath = null;

    @FXML
    private void handleSave(ActionEvent event) {
        String title = titleField.getText();
        String director = directorField.getText();
        String actors = actorsField.getText();
        String genre = genreField.getText();
        String duration = durationField.getText();
        LocalDate releaseDate = releaseDatePicker.getValue();
        String description = descriptionField.getText();

        if (title.isEmpty() || director.isEmpty() || duration.isEmpty() || releaseDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return;
        }

        String sql = "INSERT INTO movies (title, directors, actors, genres, duration, release_date, description, images) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, director);
            stmt.setString(3, actors);
            stmt.setString(4, genre);
            stmt.setString(5, duration);
            stmt.setString(6, releaseDate.toString());
            stmt.setString(7, description);
            stmt.setString(8, imagePath);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Movie added successfully!");
            handleClear();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add movie.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClear() {
        titleField.clear();
        directorField.clear();
        actorsField.clear();
        genreField.clear();
        durationField.clear();
        releaseDatePicker.setValue(null);
        descriptionField.clear();
        imagePath = null;
    }

    @FXML
    private void handleAddImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            System.out.println("Selected Image: " + imagePath);
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try{
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage,"admin/moviemanagement/movie-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
