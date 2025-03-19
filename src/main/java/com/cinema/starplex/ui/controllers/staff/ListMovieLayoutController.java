package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.GenreDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.collections.ObservableList;
import javafx.util.Duration;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

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
        HBox movieBox = new HBox(25); // Tăng khoảng cách giữa ảnh và nội dung
        movieBox.getStyleClass().add("movie-box");
        movieBox.setPrefWidth(533);
        movieBox.setMinHeight(267);
        movieBox.setMaxHeight(267);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(213);
        imageView.setImage(new Image(movie.getImage()));

        VBox detailsBox = new VBox(7);
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(307);

        // Lấy danh sách thể loại từ bảng movie_movie_genres
        String genreNames = getGenresForMovie(movie.getId());

        Text genreText = new Text("Genre: " + genreNames + "\n");
        Text titleText = new Text(movie.getTitle() + "\n");
        Text durationText = new Text("Time: " + movie.getDuration() + " minute\n");
        Text releaseDateText = new Text("Release Date: " + movie.getReleaseDate() + "\n");
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

        FlowPane showtimePane = new FlowPane();
        showtimePane.setHgap(12); // Khoảng cách ngang giữa các nút
        showtimePane.setVgap(12); // Khoảng cách dọc giữa các hàng
        showtimePane.setPrefWrapLength(4 * 80);
        // HBox showtimeBox = new HBox(12);
        ObservableList<Showtime> showtimes = movieDao.getShowtimesByMovieId(movie.getId());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Showtime showtime : showtimes) {
            Time show_time = showtime.getShowTime();
            String formattedTime = timeFormat.format(new Date(show_time.getTime())); // Chuyển thành HH:mm
            Button btnShowtime = new Button(formattedTime);
            btnShowtime.getStyleClass().add("showtime-btn");
            showtimePane.getChildren().add(btnShowtime);
            btnShowtime.setOnAction(event -> {
                // TODO: Hiển thị lịch chiếu khi click vào lịch chiếu
                FXMLLoader loader = SceneSwitcher.loadView("staff/movie_detail_layout.fxml");
                if (loader != null) {
                    MovieDetailLayout controller = loader.getController();
                    try {
                        controller.setCenterBorderPane(showtime, movie);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Parent newView = loader.getRoot();
                    AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
                    BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

                    if (mainPane != null) {
                        mainPane.setCenter(newView);
                    } else {
                        System.err.println("BorderPane with ID'mainBorderPane' not found");
                    }
                } else {
                    System.err.println("Could not load add-seat.fxml");
                }
            });
        }

        detailsBox.getChildren().add(showtimeLabel);
        detailsBox.getChildren().add(showtimePane);
        movieBox.getChildren().addAll(imageView, detailsBox);
        addZoomEffect(movieBox);

        movieBox.setOnMouseClicked(event -> {
            // TODO: Hiển thị chi tiết phim khi click vào ảnh

            FXMLLoader loader = SceneSwitcher.loadView("staff/movie_detail_layout.fxml");
            if (loader != null) {
                MovieDetailLayout controller = loader.getController();
                controller.setMovie(movie);

                Parent newView = loader.getRoot();
                AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
                BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

                if (mainPane != null) {
                    mainPane.setCenter(newView);
                } else {
                    System.err.println("BorderPane with ID 'mainBorderPane' not found");
                }
            } else {
                System.err.println("Could not load add-seat.fxml");
            }
        });

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
        zoomIn.setToX(1.08);
        zoomIn.setToY(1.08);

        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(200), movieBox);
        zoomOut.setToX(1.0);
        zoomOut.setToY(1.0);

        movieBox.setOnMouseEntered(event -> zoomIn.playFromStart());
        movieBox.setOnMouseExited(event -> zoomOut.playFromStart());
    }
}
