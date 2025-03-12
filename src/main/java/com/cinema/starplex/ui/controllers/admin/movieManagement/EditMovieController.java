package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditMovieController {
    @FXML
    private TextField titleField;
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

    private Movie movie;
    private File selectedFile;

    public void setMovie(Movie movie) {
        this.movie = movie;
        titleField.setText(movie.getTitle());
        directorField.setText(movie.getDirector());
        actorsField.setText(movie.getActors());
        genreField.setText(movie.getGenre());
        durationField.setText(String.valueOf(movie.getDuration()));
        releaseDatePicker.setValue(movie.getReleaseDate().toLocalDate());
        descriptionField.setText(movie.getDescription());

        if (movie.getImage() != null) {
            File file = new File(movie.getImage());
            if (file.exists()) {
                movieImageView.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    @FXML
    private void handleEditImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            movieImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void handleEditMovie() {
        String sql = "UPDATE movies SET title = ?, directors = ?, actors = ?, genres = ?, duration = ?, release_date = ?, description = ?, images = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titleField.getText());
            pstmt.setString(2, directorField.getText());
            pstmt.setString(3, actorsField.getText());
            pstmt.setString(4, genreField.getText());
            pstmt.setFloat(5, Float.parseFloat(durationField.getText()));
            pstmt.setDate(6, Date.valueOf(releaseDatePicker.getValue()));
            pstmt.setString(7, descriptionField.getText());

            String imagePath = movie.getImage();
            if (selectedFile != null) {
                File destFile = new File("src/main/resources/images/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagePath = destFile.getAbsolutePath();
            }

            pstmt.setString(8, imagePath);
            pstmt.setInt(9, movie.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                movie.setTitle(titleField.getText());
                movie.setDirector(directorField.getText());
                movie.setActors(actorsField.getText());
                movie.setGenre(genreField.getText());
                movie.setDuration(Float.parseFloat(durationField.getText()));
                movie.setReleaseDate(Date.valueOf(releaseDatePicker.getValue()));
                movie.setDescription(descriptionField.getText());
                movie.setImage(imagePath);

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close(); // Đóng cửa sổ sau khi lưu
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleEditClear() {
        titleField.clear();
        directorField.clear();
        actorsField.clear();
        genreField.clear();
        durationField.clear();
        releaseDatePicker.setValue(null);
        descriptionField.clear();
    }
}

