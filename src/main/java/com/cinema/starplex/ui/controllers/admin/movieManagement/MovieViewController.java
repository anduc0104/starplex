package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    @FXML
    private TextField searchField;

    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private final MovieDao movieDao = new MovieDao();

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        directorColumn.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        actorsColumn.setCellValueFactory(cellData -> cellData.getValue().actorsProperty());
        genreColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getGenres().stream()
                        .map(genre -> genre.getName())
                        .collect(Collectors.joining(", "))
        ));
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        imageColumn.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView();
            String imagePath = cellData.getValue().getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image;
                    if (imagePath.startsWith("http") || imagePath.startsWith("https")) {
                        image = new Image(imagePath);
                    } else {
                        File file = new File(new URI(imagePath)); // Chuyển đổi lại từ URI
                        if (file.exists()) {
                            image = new Image(file.toURI().toString());
                        } else {
                            System.err.println("File not found: " + imagePath);
                            image = new Image(getClass().getResource("/images/default.jpg").toExternalForm());
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
        setupSearchFilter();
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
        movieList.clear();
        ObservableList<Movie> movies = movieDao.getMovies();
        movieTable.setItems(movies);

        if (movies != null) {
            movieList.addAll(movies);
            System.out.println("Movies loaded: " + movieList.size());
        } else {
            System.out.println("Failed to load movies!");
        }

        movieTable.setItems(movieList);
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
                    System.out.println("Movie deleted: " + movie.getTitle());
                    loadMovies();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void handleSwitchAddNew(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SceneSwitcher.switchTo(stage, "admin/moviemanagement/add-movie.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        if (movieList == null) {
            System.out.println("movieList is NULL!");
            return;
        }

        System.out.println("Movie List Size: " + movieList.size());
        for (Movie m : movieList) {
            System.out.println("Movie: " + m.getTitle());
        }

        if (movieList.isEmpty()) {
            System.out.println("Movie List is empty!");
            return;
        }

        // Dùng `FilteredList` bọc `movieList` để bộ lọc hoạt động động
        FilteredList<Movie> filteredMovies = new FilteredList<>(movieList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Search: " + newValue);

            filteredMovies.setPredicate(movie -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                boolean match = movie.getTitle().toLowerCase().contains(lowerCaseFilter);

                if (match) {
                    System.out.println("Matched: " + movie.getTitle());
                }

                return match;
            });

            System.out.println("Filtered Movies: " + filteredMovies.size());
        });

        // Cập nhật danh sách sau khi lọc
        movieTable.setItems(filteredMovies);

        System.out.println("Search filter applied!");
    }
}
