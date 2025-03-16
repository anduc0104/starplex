package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.GenreDao;
import com.cinema.starplex.models.Movie;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.sql.SQLException;

public class ListMovieLayoutController {

    @FXML
    private GridPane movieGrid;

    @FXML
    private HBox showDateBox; // Thêm HBox để chứa ngày chiếu

    private MovieDao movieDao;
    private GenreDao genreDao;

    @FXML
    public void initialize() {
        movieDao = new MovieDao();
        genreDao = new GenreDao();
        loadShowDates(); // Tải danh sách ngày chiếu trước
    }

    private void loadShowDates() {
        try {
            ObservableList<String> showDates = movieDao.getAllShowDates();
            showDateBox.getChildren().clear();

            if (showDates.isEmpty()) {
                System.out.println("No show dates found!");
            }

            for (String date : showDates) {
                Button btnDate = new Button(date);
                btnDate.getStyleClass().add("showdate-btn");

                // Kiểm tra xem sự kiện click có được gán đúng không
                btnDate.setOnAction(event -> {
                    System.out.println("Clicked on the show date: " + date);
                    loadMoviesByDate(date);
                });

                showDateBox.getChildren().add(btnDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMoviesByDate(String selectedDate) {
        try {
            ObservableList<Movie> movies = movieDao.getMoviesByDate(selectedDate);
            movieGrid.getChildren().clear();
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
        HBox movieBox = new HBox(25);
        movieBox.getStyleClass().add("movie-box");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(213);
        imageView.setImage(new Image("file:./src/main/resources/images/" + movie.getImage()));

        VBox detailsBox = new VBox(7);
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(307);

        String genreNames = getGenresForMovie(movie.getId());

        Text titleText = new Text(movie.getTitle() + "\n");
        Text genreText = new Text("Genre: " + genreNames + "\n");
        Text durationText = new Text("Time: " + movie.getDuration() + " minute\n");
        Text descriptionText = new Text(movie.getDescription());

        titleText.getStyleClass().add("movie-title");
        genreText.getStyleClass().add("movie-info");
        durationText.getStyleClass().add("movie-info");
        descriptionText.getStyleClass().add("movie-description");

        textFlow.getChildren().addAll(titleText, genreText, durationText, descriptionText);
        detailsBox.getChildren().add(textFlow);

        // Thêm lịch chiếu
        Text showtimeLabel = new Text("Showtime");
        showtimeLabel.getStyleClass().add("showtime-label");

        HBox showtimeBox = new HBox(12);
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

        detailsBox.getChildren().addAll(showtimeLabel, showtimeBox);
        movieBox.getChildren().addAll(imageView, detailsBox);
        addZoomEffect(movieBox);

        return movieBox;
    }

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
        zoomIn.setToX(1.08);
        zoomIn.setToY(1.08);

        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(200), movieBox);
        zoomOut.setToX(1.0);
        zoomOut.setToY(1.0);

        movieBox.setOnMouseEntered(event -> zoomIn.playFromStart());
        movieBox.setOnMouseExited(event -> zoomOut.playFromStart());
    }
}