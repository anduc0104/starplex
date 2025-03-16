package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.GenreDao;
import com.cinema.starplex.models.Movie;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private GenreDao genreDao;

    @FXML
    public void initialize() {
        movieDao = new MovieDao();
        genreDao = new GenreDao();
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
        movieBox.getStyleClass().add("movie-box");
        movieBox.setPrefWidth(400);
        movieBox.setMinHeight(200);
        movieBox.setMaxHeight(200);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(160);
        imageView.setImage(new Image("file:./src/main/resources/images/" + movie.getImage()));

        VBox detailsBox = new VBox(5);
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(230);

        // Lấy danh sách thể loại từ bảng movie_movie_genres
        String genreNames = getGenresForMovie(movie.getId());

        Text genreText = new Text("Genre: " + genreNames + "\n");
        Text titleText = new Text(movie.getTitle() + "\n");
        Text durationText = new Text("Time: " + movie.getDuration() + " minute\n");
        Text releaseDateText = new Text("Premiere: " + movie.getReleaseDate() + "\n");
        Text descriptionText = new Text(movie.getDescription());

        genreText.getStyleClass().add("movie-info");
        titleText.getStyleClass().add("movie-title");
        durationText.getStyleClass().add("movie-info");
        releaseDateText.getStyleClass().add("movie-info");
        descriptionText.getStyleClass().add("movie-description");

        textFlow.getChildren().addAll(genreText, titleText, durationText, releaseDateText, descriptionText);
        detailsBox.getChildren().add(textFlow);
        // Thêm lịch chiếu
        Text showtimeLabel = new Text("Showtime");
        showtimeLabel.getStyleClass().add("showtime-label");

        HBox showtimeBox = new HBox(10);
        try {
            ObservableList<String> showtimes = movieDao.getShowtimesByMovieId(movie.getId());
            for (String showtime : showtimes) {
                Button btnShowtime = new Button(showtime);
                btnShowtime.getStyleClass().add("showtime-btn");
                showtimeBox.getChildren().add(btnShowtime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        detailsBox.getChildren().add(showtimeLabel);
        detailsBox.getChildren().add(showtimeBox);
        movieBox.getChildren().addAll(imageView, detailsBox);
        addZoomEffect(movieBox);

        return movieBox;
    }

    // Phương thức lấy danh sách tên thể loại theo ID phim
    private String getGenresForMovie(int movieId) {
        try {
            ObservableList<String> genres = genreDao.getGenresByMovieId(movieId);
            return genres.isEmpty() ? "Unknown" : String.join(", ", genres);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private void addZoomEffect(HBox movieBox) {
        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(200), movieBox);
        zoomIn.setToX(1.05);
        zoomIn.setToY(1.05);

        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(200), movieBox);
        zoomOut.setToX(1.0);
        zoomOut.setToY(1.0);

        movieBox.setOnMouseEntered(event -> zoomIn.playFromStart());
        movieBox.setOnMouseExited(event -> zoomOut.playFromStart());
    }
}