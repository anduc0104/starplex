package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.models.Movie;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import java.sql.SQLException;

public class ListMovieLayoutController {

    @FXML
    private GridPane movieGrid;

    private MovieDao movieDao;

    @FXML
    public void initialize() {
        movieDao = new MovieDao();
        loadMovies();
    }

    private void loadMovies() {
        try {
            ObservableList<Movie> movies = movieDao.getMovies();
            int row = 0;
            int column = 0;

            for (Movie movie : movies) {
                HBox movieBox = createMovieBox(movie);
                movieGrid.add(movieBox, column, row);

                column++;
                if (column >= 2) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createMovieBox(Movie movie) {
        HBox movieBox = new HBox(20);
        movieBox.getStyleClass().add("movie-box"); // Thêm class CSS vào movieBox
        movieBox.setPrefWidth(400);
        movieBox.setMinHeight(200);
        movieBox.setMaxHeight(200);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(160);
        imageView.setImage(new Image("file:./src/main/resources/images/" + movie.getImage()));

        VBox detailsBox = new VBox(5);

        // Sử dụng TextFlow để tự động xuống dòng
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(230);// Giới hạn chiều rộng cho TextFlow

        Text genreText = new Text("Thể loại: " + movie.getGenre() + "\n"); //insert từ bảng movie_types & movie_genres(nma chưa có dữ liệu trong Dao nên chưa insert đc)
        Text titleText = new Text(movie.getTitle() + "\n");
        Text durationText = new Text("Thời gian: " + movie.getDuration() + " phút\n");
        Text releaseDateText = new Text("Khởi chiếu: " + movie.getReleaseDate() + "\n");
        Text descriptionText = new Text(movie.getDescription());

        // Thêm class CSS vào các Text
        genreText.getStyleClass().add("movie-info");
        titleText.getStyleClass().add("movie-title");
        durationText.getStyleClass().add("movie-info");
        releaseDateText.getStyleClass().add("movie-info");
        descriptionText.getStyleClass().add("movie-description");

        // Thêm các Text vào TextFlow
        textFlow.getChildren().addAll(genreText, titleText, durationText, releaseDateText, descriptionText);
        detailsBox.getChildren().add(textFlow);

        movieBox.getChildren().addAll(imageView, detailsBox);
        addZoomEffect(movieBox);

        return movieBox;
    }

    private void addZoomEffect(HBox movieBox) {
        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(200), movieBox);
        zoomIn.setToX(1.05);  // Tăng kích thước 5%
        zoomIn.setToY(1.05);

        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(200), movieBox);
        zoomOut.setToX(1.0);  // Trở về kích thước ban đầu
        zoomOut.setToY(1.0);

        movieBox.setOnMouseEntered(event -> zoomIn.playFromStart());
        movieBox.setOnMouseExited(event -> zoomOut.playFromStart());
    }
}