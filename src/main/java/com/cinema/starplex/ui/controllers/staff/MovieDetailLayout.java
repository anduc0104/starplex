package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.GenreDao;
import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class MovieDetailLayout {
    @FXML
    public Text title;
    @FXML
    public Text genre;
    @FXML
    public Text duration;
    @FXML
    public Text releaseDate;
    @FXML
    public Text description;
    public BorderPane movieLayoutBorderPane;
    @FXML
    public ImageView image;

    private Movie selectedMovie;
    private MovieDao movieDao;
    private GenreDao genreDao;

    @FXML
    public void initialize() {
        movieDao = new MovieDao();
        genreDao = new GenreDao();
    }

    public void setMovie(Movie movie) {
        this.selectedMovie = movie;
        System.out.println("movie selected layout main"+movie);
        title.setText(selectedMovie.getTitle());
        genre.setText(getGenresForMovie(movie.getId()));
        duration.setText(selectedMovie.getDuration());
        releaseDate.setText(selectedMovie.getReleaseDate());
        description.setText(selectedMovie.getDescription());
        image.setImage(new Image(selectedMovie.getImage()));

        getListShowtime();
    }

    public void setCenterBorderPane(Showtime showtime, Movie movie) throws SQLException {
        setMovie(movie);
        FXMLLoader loader = SceneSwitcher.loadView("staff/movie_detail.fxml");
        if (loader != null) {
            MovieDetail controller = loader.getController();
            controller.setShowtime(showtime, selectedMovie);

            Parent newView = loader.getRoot();
            movieLayoutBorderPane.setCenter(newView);
        } else {
            System.err.println("Could not load add-seat.fxml");
        }

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

    private void getListShowtime() {
        // Tạo HBox chứa FlowPane
        HBox showtimeBox = new HBox(16);
        showtimeBox.setAlignment(Pos.TOP_CENTER);
        showtimeBox.setStyle("-fx-margin-top: 32px;");

// Tạo FlowPane để hiển thị showtime
        FlowPane showtimePane = new FlowPane();
        showtimePane.setHgap(12);
        showtimePane.setVgap(12);
        showtimeBox.setPadding(new Insets(32, 0, 0, 0));
        showtimePane.setPrefWrapLength(5 * 80); // Mỗi hàng tối đa 4 nút (80px mỗi nút)

// Thêm FlowPane vào HBox
        showtimeBox.getChildren().add(showtimePane);

// Lấy danh sách lịch chiếu từ database
        ObservableList<Showtime> showtimes = movieDao.getShowtimesByMovieId(selectedMovie.getId());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Showtime showtime : showtimes) {
            Time show_time = showtime.getShowTime();
            String formattedTime = timeFormat.format(new Date(show_time.getTime())); // Chuyển thành HH:mm

            Button btnShowtime = new Button(formattedTime);
            btnShowtime.getStyleClass().add("showtime-btn");

            showtimePane.getChildren().add(btnShowtime);

            btnShowtime.setOnAction(event -> {
                FXMLLoader loader = SceneSwitcher.loadView("staff/movie_detail.fxml");
                if (loader != null) {
                    MovieDetail controller = loader.getController();
                    try {
                        controller.setShowtime(showtime, selectedMovie);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Parent newView = loader.getRoot();
                    movieLayoutBorderPane.setCenter(newView);
                } else {
                    System.err.println("Could not load add-seat.fxml");
                }
            });
        }
        movieLayoutBorderPane.setCenter(showtimeBox);
    }
}
