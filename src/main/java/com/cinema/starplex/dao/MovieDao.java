package com.cinema.starplex.dao;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDao implements BaseDao<Movie> {
    static Connection conn = new DatabaseConnection().getConn();

    public ObservableList<Movie> getMovies() throws SQLException {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT m.* FROM movies m JOIN showtimes s ON m.id = s.movie_id WHERE DATE(s.show_date) = CURDATE()";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int movieId = rs.getInt("id");
                List<Genre> genres = getGenresByMovieId(movieId);

                // Tạo Movie với các thuộc tính đã được khởi tạo trong constructor
                Movie movie = new Movie(
                        movieId, // Truyền trực tiếp id
                        rs.getString("title"),
                        FXCollections.observableArrayList(genres), // Truyền danh sách genre
                        rs.getString("duration"),
                        rs.getString("release_date"),
                        rs.getString("description"),
                        rs.getString("images")
                );

                movies.add(movie);
            }
        }
        return movies;
    }

    public List<Genre> getGenresByMovieId(int movieId) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.id, g.name FROM movie_genres g " +
                "JOIN movie_movie_genres mt ON g.id = mt.genre_id " +
                "WHERE mt.movie_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Chuyển đổi int và String sang IntegerProperty và StringProperty
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))));
            }
        }
        return genres;
    }

    public ObservableList<Showtime> getShowtimesByMovieId(int movieId) throws SQLException {
        ObservableList<Showtime> showtimes = FXCollections.observableArrayList();
        String query = "SELECT * FROM showtimes WHERE movie_id = ?  AND show_date = CURDATE() ORDER BY show_time";

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Chỉ lấy giờ:phút

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, movieId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer movie_id = rs.getInt("movie_id");
                Integer roomId = rs.getInt("room_id");
                Time sqlTime = rs.getTime("show_time"); // Lấy dữ liệu kiểu TIME
//                String formattedTime = timeFormat.format(new Date(sqlTime.getTime())); // Chuyển thành HH:mm
                showtimes.add(new Showtime(id, movie_id, roomId, sqlTime));
            }
        }
        return showtimes;
    }

    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies (title, duration, release_date, description, images) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTitle());
            // stmt.setString(2, director);
            // stmt.setString(3, actors);
            stmt.setString(2, entity.getDuration());
            stmt.setString(3, entity.getReleaseDate().toString());
            stmt.setString(4, entity.getDescription());
            stmt.setString(5, entity.getImage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding seats fails, no lines are affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insert(Movie entity) {
        return false;
    }

    @Override
    public void update(Movie entity) {
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM movies WHERE id =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting chair: " + e.getMessage());
        }
    }

    @Override
    public Movie findById(long id) {
        return null;
    }

    @Override
    public ObservableList<Movie> findAll() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String query = "SELECT * FROM movies";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int movieId = resultSet.getInt("id");
                    List<Genre> genres = getGenresByMovieId(movieId);
                    Movie movie = new Movie(
                            movieId, // Truyền trực tiếp id
                            resultSet.getString("title"),
                            FXCollections.observableArrayList(genres), // Truyền danh sách genre
                            resultSet.getString("duration"),
                            resultSet.getString("release_date"),
                            resultSet.getString("description"),
                            resultSet.getString("images")
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Movie mapResultSetToMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        List<Genre> genres = getGenresByMovieId(resultSet.getInt("id"));
        movie.setId(resultSet.getInt("id"));
        movie.setTitle(resultSet.getString("title"));
        movie.setDuration(resultSet.getString("duration"));
        movie.setReleaseDate(resultSet.getString("release_date"));
        movie.setDescription(resultSet.getString("description"));
        return movie;
    }
}