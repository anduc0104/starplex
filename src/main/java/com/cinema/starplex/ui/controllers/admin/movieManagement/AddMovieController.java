package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddMovieController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField actorsField;
    @FXML
    private CheckComboBox<String> genreCheckComboBox;
    @FXML
    private TextField durationField;
    @FXML
    private DatePicker releaseDatePicker;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private ImageView movieImageView;
    @FXML
    private Button addImageButton;

    private String imagePath = null;
    private MovieDao movieDao;

    @FXML
    private void initialize() {
        movieDao = new MovieDao();
        loadGenres();
    }

    private void loadGenres() {
        String sql = "SELECT name FROM movie_genres";
        try (Connection conn = new DatabaseConnection().getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genreCheckComboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String title = titleField.getText();
//        String director = directorField.getText();
//        String actors = actorsField.getText();
        List<String> selectedGenres = new ArrayList<>(genreCheckComboBox.getCheckModel().getCheckedItems());
        String duration = durationField.getText();
        LocalDate releaseDate = releaseDatePicker.getValue();
        String description = descriptionField.getText();

        if (title.isEmpty() || duration.isEmpty() || releaseDate == null || selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return;
        }

        String sql = "INSERT INTO movies (title, duration, release_date, description, images) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn =new DatabaseConnection().getConn();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
//            stmt.setString(2, director);
//            stmt.setString(3, actors);
            stmt.setString(2, duration);
            stmt.setString(3, releaseDate.toString());
            stmt.setString(4, description);
            stmt.setString(5, imagePath);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Movies cannot be added\n.");
                return;
            }

            int movieId;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movieId = rs.getInt(1);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Unable to get movie ID\n.");
                    return;
                }
            }

            insertMovieGenres(conn, movieId, selectedGenres);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Movie added!\n");
            handleClear();
            returnToMovieView(event);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add movie.");
        }
    }
//    @FXML
//    private void handleSave(ActionEvent event) {
//        String title = titleField.getText();
////        String director = directorField.getText();
////        String actors = actorsField.getText();
//        List<String> selectedGenres = new ArrayList<>(genreCheckComboBox.getCheckModel().getCheckedItems());
//        String duration = durationField.getText();
//        LocalDate releaseDate = releaseDatePicker.getValue();
//        String description = descriptionField.getText();
//
//        if (title.isEmpty() || duration.isEmpty() || releaseDate == null || selectedGenres.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
//            return;
//        }
//
//        String sql = "INSERT INTO movies (title, duration, release_date, description, images) VALUES (?, ?, ?, ?, ?)";
//
//        try {
//            movieDao.save(new Movie(title, duration, releaseDate, description, imagePath));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add movie.");
//        }
//    }

    private void insertMovieGenres(Connection conn, int movieId, List<String> genres) throws SQLException {
        String sqlInsertMovieType = "INSERT INTO movie_movie_genres (movie_id, genre_id) VALUES (?, ?)";
        for (String genre : genres) {
            int genreId = getGenreId(conn, genre);
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsertMovieType)) {
                stmt.setInt(1, movieId);
                stmt.setInt(2, genreId);
                stmt.executeUpdate();
            }
        }
    }

    private int getGenreId(Connection conn, String genreName) throws SQLException {
        String sqlSelect = "SELECT id FROM movie_genres WHERE name = ?";
        String sqlInsert = "INSERT INTO movie_genres (name) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
            stmt.setString(1, genreName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtInsert.setString(1, genreName);
            stmtInsert.executeUpdate();

            try (ResultSet rs = stmtInsert.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Unable to get genre ID.\n");
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClear() {
        titleField.clear();
//        directorField.clear();
//        actorsField.clear();
        durationField.clear();
        releaseDatePicker.setValue(null);
        descriptionField.clear();
        genreCheckComboBox.getCheckModel().clearChecks();
        movieImageView.setImage(null);
        imagePath = null;
    }

    @FXML
    private void handleAddImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            movieImageView.setImage(new Image(imagePath));
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        returnToMovieView(actionEvent);
    }

    private void returnToMovieView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/moviemanagement/movie-view.fxml"));
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