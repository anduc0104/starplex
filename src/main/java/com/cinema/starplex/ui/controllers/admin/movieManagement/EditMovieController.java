package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class EditMovieController {
    @FXML
    private TextField titleField;
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
    private Button handleBack;
    @FXML
    private ImageView movieImageView;
    @FXML
    private Button addImageButton;

    private Movie movie;
    private String imagePath = null;
    private File selectedImageFile = null;
    private static final String IMAGE_DIR = "src/main/resources/images/";

    @FXML
    private void initialize() {
        loadGenres();
    }

    private void loadGenres() {
        genreCheckComboBox.getItems().clear();
        String sql = "SELECT name FROM movie_genres";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genreCheckComboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load genres.");
        }
    }

    public void setMovie(Movie movie) {
        if (movie == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Movie cannot be null.");
            return;
        }
        this.movie = movie;
        titleField.setText(movie.getTitle());
        if (genreCheckComboBox.getItems().isEmpty()) {
            loadGenres();
        }
        for (Genre genre : movie.getGenres()) {
            genreCheckComboBox.getCheckModel().check(genre.getName());
        }
        durationField.setText(String.valueOf(movie.getDuration()));
        releaseDatePicker.setValue(LocalDate.parse(movie.getReleaseDate()));
        descriptionField.setText(movie.getDescription());

        if (movie.getImage() != null && !movie.getImage().isEmpty()) {
            try {
                String imagePath = movie.getImage();
                if (!imagePath.startsWith("file:/")) {
                    imagePath = new File(imagePath).toURI().toString();
                }
                movieImageView.setImage(new Image(imagePath));
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Image Load Error", "Error loading image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            imagePath = selectedImageFile.toURI().toString();
            movieImageView.setImage(new Image(imagePath));
        }
    }

    @FXML
    private void handleEditMovie(ActionEvent event) {
        if (titleField.getText().isEmpty() || durationField.getText().isEmpty() ||
                releaseDatePicker.getValue() == null || descriptionField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return;
        }

        String sql = "UPDATE movies SET title = ?, duration = ?, release_date = ?, description = ?, images = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titleField.getText());
            pstmt.setFloat(2, Float.parseFloat(durationField.getText()));
            pstmt.setDate(3, Date.valueOf(releaseDatePicker.getValue()));
            pstmt.setString(4, descriptionField.getText());

            // Kiểm tra hình ảnh
            String imagePath = movie.getImage(); // Lưu đường dẫn hình ảnh cũ
            if (selectedImageFile != null) {
                File destFile = new File(IMAGE_DIR, selectedImageFile.getName());
                Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagePath = destFile.toURI().toString(); // Cập nhật đường dẫn hình ảnh mới
            }
            pstmt.setString(5, imagePath); // Cập nhật đường dẫn hình ảnh mới
            pstmt.setInt(6, movie.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Cập nhật thông tin phim
                movie.setTitle(titleField.getText());
                movie.setDuration(String.valueOf(Float.parseFloat(durationField.getText())));
                movie.setReleaseDate(releaseDatePicker.getValue().toString());
                movie.setDescription(descriptionField.getText());
                movie.setImage(imagePath); // Cập nhật hình ảnh mới

                // Cập nhật thể loại
                String deleteGenresSql = "DELETE FROM movie_movie_genres WHERE movie_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteGenresSql)) {
                    deleteStmt.setInt(1, movie.getId());
                    deleteStmt.executeUpdate();
                }

                String insertGenresSql = "INSERT INTO movie_movie_genres (movie_id, genre_id) VALUES (?, ?)";
                for (String genreName : genreCheckComboBox.getCheckModel().getCheckedItems()) {
                    try {
                        // Lấy ID thể loại từ tên thể loại
                        int genreId = getGenreId(conn, genreName); // Gọi phương thức để lấy ID thể loại
                        // Chèn thể loại vào bảng movie_movie_genres
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertGenresSql)) {
                            insertStmt.setInt(1, movie.getId());
                            insertStmt.setInt(2, genreId);
                            insertStmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert genre: " + genreName);
                    }
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Movie edited!");
                handleEditClear();
                returnToMovieView(event);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Changes", "No changes were made to the movie.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to edit movie.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File Error", "Failed to save image.");
        }
    }

    private int getGenreId(Connection conn, String genreName) throws SQLException {
        String sql = "SELECT id FROM movie_genres WHERE name =?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genreName);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Genre not found: " + genreName);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @FXML
    private void handleEditClear() {
        titleField.clear();
        genreCheckComboBox.getCheckModel().clearChecks();
        durationField.clear();
        releaseDatePicker.setValue(null);
        descriptionField.clear();
        movieImageView.setImage(null);
        selectedImageFile = null;
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        returnToMovieView(actionEvent);
    }

    private void returnToMovieView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/moviemanagement/movie-view.fxml"));
            Parent seatView = loader.load();

            AnchorPane root = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}