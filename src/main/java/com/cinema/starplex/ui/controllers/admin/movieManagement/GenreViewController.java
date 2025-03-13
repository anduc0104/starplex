package com.cinema.starplex.ui.controllers.admin.movieManagement;

import com.cinema.starplex.dao.GenreDao;
import com.cinema.starplex.models.Genre;
import com.cinema.starplex.util.DatabaseConnection;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class GenreViewController {
    @FXML
    private TableView<Genre> genreTable;

    @FXML
    private TableColumn<Genre, Integer> idColumn;

    @FXML
    private TableColumn<Genre, String> genreColumn;

    @FXML
    private TableColumn<Genre, Void> actionColumn;

    @FXML
    private TextField searchField;

    private final ObservableList<Genre> genreList = FXCollections.observableArrayList();
    private final GenreDao genreDao = new GenreDao();

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        setupActionColumn();
        loadGenres();
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
                    Genre genre = getTableView().getItems().get(getIndex());
                    handleEdit(genre);
                });
                deleteButton.setOnAction(event -> {
                    Genre genre = getTableView().getItems().get(getIndex());
                    handleDelete(genre);
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

        if (!genreTable.getColumns().contains(actionColumn)) {
            genreTable.getColumns().add(actionColumn);
        }
    }

    private void loadGenres() throws SQLException {
        genreList.clear();
        ObservableList<Genre> genres = genreDao.getGenres();
        genreTable.setItems(genres);

        if (genres != null) {
            genreList.addAll(genres);
            System.out.println("Genres loaded: " + genreList.size());
        } else {
            System.out.println("Failed to load genres!");
        }

        genreTable.setItems(genreList);
    }

    private void handleEdit(Genre genre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/moviemanagement/edit-genre.fxml"));
            Parent root = loader.load();

            EditGenreController controller = loader.getController();
            controller.setGenre(genre); // Truyền dữ liệu phim vào form

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Genre");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Cập nhật TableView sau khi chỉnh sửa xong
            genreTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Genre genre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Are you sure you want to delete this movie?");
        alert.setContentText(genre.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            genreDao.delete(genre);
            String sql = "DELETE FROM movie_genres WHERE id = ?";

            try (Connection conn = DatabaseConnection.getConn();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, genre.getId());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Genre deleted: " + genre.getName());
                    loadGenres();
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
            SceneSwitcher.switchTo(stage, "admin/moviemanagement/add-genre.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        if (genreList == null) {
            System.out.println("genreList is NULL!");
            return;
        }

        System.out.println("Genre List Size: " + genreList.size());
        for (Genre m : genreList) {
            System.out.println("Genre: " + m.getName());
        }

        if (genreList.isEmpty()) {
            System.out.println("Genre List is empty!");
            return;
        }

        // Dùng `FilteredList` bọc `movieList` để bộ lọc hoạt động động
        FilteredList<Genre> filteredMovies = new FilteredList<>(genreList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Search: " + newValue);

            filteredMovies.setPredicate(movie -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                boolean match = movie.getName().toLowerCase().contains(lowerCaseFilter);

                if (match) {
                    System.out.println("Matched: " + movie.getName());
                }

                return match;
            });

            System.out.println("Filtered Genres: " + filteredMovies.size());
        });

        // Cập nhật danh sách sau khi lọc
        genreTable.setItems(filteredMovies);

        System.out.println("Search filter applied!");
    }

    public void handleAdd(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/moviegenremanagement/add-genre.fxml");
        if (loader != null) {
            Parent newView = loader.getRoot(); // Lấy Root từ FXMLLoader
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");
            mainPane.setCenter(newView); // Thay đổi nội dung của center
        } else {
            System.err.println("Failed to load");
        }
    }
}