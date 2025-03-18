package com.cinema.starplex.ui.controllers.staff.staffDetail;

import com.cinema.starplex.dao.MovieDao;
import com.cinema.starplex.dao.MovieMovieGenreDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.MovieGenre;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HompageStaffConroller implements Initializable {
    @FXML
    private ImageView movieImageField;

    @FXML
    private Label movieTitleField;

    @FXML
    private Label movieDurationField;

    @FXML
    private Label movieDirectorField;

    @FXML
    private Label movieActorField;

    @FXML
    private Label movieReleaseDateField;

    @FXML
    private TextArea movieDescriptionField;

    @FXML
    private Button startTimeField;

    @FXML
    private GridPane showtimeGrid;

    @FXML
    private HBox dateSelectionBox;

    private MovieDao movieDao;
    private ShowTimeDao showTimeDao;
    private MovieMovieGenreDao movieGenreDao;

    private Movie currentMovie;
    private List<Showtime> currentShowtimes = new ArrayList<>();
    private LocalDate selectedDate = LocalDate.now();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieDao = new MovieDao();
        showTimeDao = new ShowTimeDao();
        movieGenreDao = new MovieMovieGenreDao();

        generateDateButtons();
        loadLatestMovie();
    }

    private void generateDateButtons() {
        dateSelectionBox.getChildren().clear();
        //generate nut date cho 7 ngay tiep theo
        for (int i=0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            Button dateButton = new Button(formatDateButtonText(date));

            dateButton.setOnAction(event -> {
                selectedDate = date;
                loadShowtimes();
                highlightSelectedDateButton(dateButton);
            });
            //mac dinh chon ngay dau tien
            if (i==0) {
                dateButton.getStyleClass().add("selected-date");
            }

            dateSelectionBox.getChildren().add(dateButton);
        }
    }

    private String formatDateButtonText(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E - dd");
        return date.format(formatter);
    }

    private void highlightSelectedDateButton(Button selectedButton) {
        for (var node : dateSelectionBox.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.getStyleClass().remove("selected-date");
            }
        }
        selectedButton.getStyleClass().add("selected-date");
    }

    private void loadLatestMovie() {
        Movie movie = movieDao.getLatestMovie();
        if (movie != null) {
            currentMovie = movie;
            displayMovieDetails(movie);
            loadShowtimes();
        }
    }

    private void displayMovieDetails(Movie movie) {
        movieTitleField.setText(movie.getTitle());
        movieDurationField.setText(getGenreString(movie) + " - " + movie.getDuration() + " minutes");
        movieDescriptionField.setText(movie.getDescription());
        movieReleaseDateField.setText("Start time date: " + formatReleaseDate(movie.getReleaseDate()));

        // Load movie image if available
        if (movie.getImage() != null && !movie.getImage().isEmpty()) {
            try {
                Image image = new Image(movie.getImage());
                movieImageField.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }
    }

    private String getGenreString(Movie movie) {
        List<MovieGenre> genres = movieGenreDao.getGenresByMovieId(movie.getId());
        if (genres.isEmpty()) {
            return "Unknown";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            sb.append(genres.get(i).getName());
            if (i < genres.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String formatReleaseDate(LocalDate date) {
        if (date == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private void loadShowtimes() {
        if (currentMovie == null) return;

        // Clear existing showtimes
        showtimeGrid.getChildren().clear();
        int row = 1; // Start from row 1 (row 0 has the warning label)
        int col = 0;

        // Get showtimes for current movie and selected date
        currentShowtimes = showTimeDao.getShowtimesByMovieAndDate(currentMovie.getId(), selectedDate);

        for (Showtime showtime : currentShowtimes) {
            Button showtimeButton = createShowtimeButton(showtime);

            showtimeGrid.add(showtimeButton, col, row);

            // Move to next column or row
            col++;
            if (col > 3) { // 4 columns per row
                col = 0;
                row++;
            }
        }
    }

    private Button createShowtimeButton(Showtime showtime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Chuyển đổi Timestamp thành LocalTime
        Timestamp timestamp = showtime.getStartTime();
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();

        // Format thời gian
        String timeText = time.format(formatter);

        // Tạo button
        Button button = new Button(timeText);
//        button.setOnAction(event -> handleShowtimeSelection(showtime));
        return button;
    }

    private void handleShowtimeSelection(Showtime showtime) throws IOException {
        // Navigate to ticket booking screen or show more details
        System.out.println("Selected showtime: " + showtime.getId());
        //call staffDetail function
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/staff/staff-detail.fxml"));
        Parent root = loader.load();
        StaffDetailController controller = loader.getController();
        controller.setShowtime(showtime);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Show time");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleMovieSelection(ActionEvent event) {
        // TODO: Implement movie browsing/selection functionality
    }
    private void staffDetail(Showtime showtime) throws IOException {
//        Stage stage = (Stage) showtimeGrid.getScene().getWindow();
//        SceneSwitcher.switchTo(stage, "/com/cinema/starplex/staff/staff-detail.fxml");
    }
}

