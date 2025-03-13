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

    private Movie movie;
    private String imagePath = null;
    private File selectedFile = null;
    private static final String IMAGE_DIR = "src/main/resources/images/"; // Thư mục lưu ảnh

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
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        titleField.setText(movie.getTitle());
//        directorField.setText(movie.getDirector());
//        actorsField.setText(movie.getActors());
        if (genreCheckComboBox.getItems().isEmpty()) {
            loadGenres();
        }
        for (Genre genre : movie.getGenres()) {
            genreCheckComboBox.getCheckModel().check(genre.getName()); // Giả sử Genre có phương thức getName()
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
                System.out.println("Lỗi khi load ảnh: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            movieImageView.setImage(new Image(imagePath));
        }
    }

    @FXML
    private void handleEditMovie(ActionEvent event) {
        String sql = "UPDATE movies SET title = ?, genres = ?, duration = ?, release_date = ?, description = ?, images = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titleField.getText());
//            pstmt.setString(2, directorField.getText());
//            pstmt.setString(3, actorsField.getText());
            String selectedGenresString = genreCheckComboBox.getCheckModel().getCheckedItems()
                    .stream().collect(Collectors.joining(", "));
            ObservableList<Genre> selectedGenres = FXCollections.observableArrayList();
            for (String genreName : genreCheckComboBox.getCheckModel().getCheckedItems()) {
                selectedGenres.add(new Genre(new SimpleIntegerProperty(0), new SimpleStringProperty(genreName)));
            }


            pstmt.setString(2, selectedGenresString); // Lưu vào database
            movie.setGenres(selectedGenres); // Cập nhật Movie object
            pstmt.setFloat(3, Float.parseFloat(durationField.getText()));
            pstmt.setDate(4, Date.valueOf(releaseDatePicker.getValue()));
            pstmt.setString(5, descriptionField.getText());

            String imagePath = movie.getImage();
            if (selectedFile != null) {
                File destFile = new File(IMAGE_DIR, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagePath = destFile.toURI().toString();
            }

            pstmt.setString(6, imagePath);
            pstmt.setInt(7, movie.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                movie.setTitle(titleField.getText());
//                movie.setDirector(directorField.getText());
//                movie.setActors(actorsField.getText());
                movie.setGenres(selectedGenres);
                movie.setDuration(String.valueOf(Float.parseFloat(durationField.getText())));
                movie.setReleaseDate(releaseDatePicker.getValue().toString());
                movie.setDescription(descriptionField.getText());
                movie.setImage(imagePath);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Movie edited!\n");
                handleEditClear();
                returnToMovieView(event);
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
//        directorField.clear();
//        actorsField.clear();
        genreCheckComboBox.getCheckModel().clearChecks();
        durationField.clear();
        releaseDatePicker.setValue(null);
        descriptionField.clear();
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/moviemanagement/movie-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent event) {
        returnToMovieView(event);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
