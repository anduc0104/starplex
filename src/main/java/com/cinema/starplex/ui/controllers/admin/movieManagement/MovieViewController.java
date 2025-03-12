package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class MovieViewController {

    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, Number> idColumn;
    @FXML
    private TableColumn<Movie, ImageView> imageColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> directorColumn;
    @FXML
    private TableColumn<Movie, String> actorsColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, String> durationColumn;
    @FXML
    private TableColumn<Movie, String> releaseDateColumn;
    @FXML
    private TableColumn<Movie, String> descriptionColumn;
    @FXML
    private TableColumn<Movie, Void> actionColumn;


    private final MovieDao movieDao = new MovieDao();

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        directorColumn.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        actorsColumn.setCellValueFactory(cellData -> cellData.getValue().actorsProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        durationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDuration())));

        releaseDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReleaseDate().toString()));

        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());


        imageColumn.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView();
            String imagePath = cellData.getValue().getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image;
                    if (imagePath.startsWith("http") || imagePath.startsWith("https")) {
                        // Nếu là URL trực tuyến
                        image = new Image(imagePath);
                    } else {
                        // Nếu là đường dẫn cục bộ
                        File file = new File(imagePath);
                        if (file.exists()) {
                            image = new Image(file.toURI().toString()); // Chuyển đổi thành "file:/"
                        } else {
                            System.err.println("File not found: " + imagePath);
                            image = new Image(getClass().getResource("/images/default.jpg").toExternalForm()); // Ảnh mặc định
                        }
                    }
                    imageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Lỗi khi load ảnh: " + e.getMessage());
                }
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            return new SimpleObjectProperty<>(imageView);
        });
        setupActionColumn();
        loadMovies();
    }

    private void setupActionColumn() {
        if (actionColumn == null) {
            actionColumn = new TableColumn<>("Action"); // Khởi tạo cột nếu chưa có
        }
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    handleEdit(movie);
                });
                deleteButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    handleDelete(movie);
                });
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        if (!movieTable.getColumns().contains(actionColumn)) {
            movieTable.getColumns().add(actionColumn);
        }

    }

    private void loadMovies() throws SQLException {
        ObservableList<Movie> movies = movieDao.getMovies();
        movieTable.setItems(movies);
    }

    private void handleEdit(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/moviemanagement/edit-movie.fxml"));
            Parent root = loader.load();

            EditMovieController controller = loader.getController();
            controller.setMovie(movie); // Truyền dữ liệu phim vào form

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Cập nhật TableView sau khi chỉnh sửa xong
            movieTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Movie movie) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Are you sure you want to delete this movie?");
        alert.setContentText(movie.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            movieDao.delete(movie);
            String sql = "DELETE FROM movies WHERE id = ?";

            try (Connection conn = DatabaseConnection.getConn();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, movie.getId());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    movieTable.getItems().remove(movie);
                    System.out.println("Movie deleted: " + movie.getTitle());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


}
