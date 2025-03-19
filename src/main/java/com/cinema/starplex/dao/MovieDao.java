package com.cinema.starplex.dao;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDao implements BaseDao<Movie> {

    private final Connection conn;

    public MovieDao() {
        conn = DatabaseConnection.getConn();
    }

    public ObservableList<Movie> getMovies() throws SQLException {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String sql = "SELECT id, title, duration, release_date, description, images FROM movies";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int movieId = rs.getInt("id");
                List<Genre> genres = getGenresByMovieId(movieId);

                Movie movie = new Movie(
                        movieId,
                        rs.getString("title"),
                        FXCollections.observableArrayList(genres),
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
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))
                ));
            }
        }
        return genres;
    }

    public ObservableList<String> getShowtimesByMovieId(int movieId) throws SQLException {
        ObservableList<String> showtimes = FXCollections.observableArrayList();
        String query = "SELECT show_time FROM showtimes WHERE movie_id = ? ORDER BY show_time";

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, movieId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Time sqlTime = rs.getTime("show_time");
                String formattedTime = timeFormat.format(new Date(sqlTime.getTime()));
                showtimes.add(formattedTime);
            }
        }
        return showtimes;
    }

    public ObservableList<String> getAllShowDates() throws SQLException {
        ObservableList<String> showDates = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT show_date FROM showtimes ORDER BY show_date";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                showDates.add(rs.getString("show_date"));
            }
        }
        return showDates;
    }

    public ObservableList<Movie> getMoviesByDate(String date) throws SQLException {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT m.id, m.title, m.duration, m.release_date, m.description, m.images " +
                "FROM movies m JOIN showtimes s ON m.id = s.movie_id WHERE s.show_date = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int movieId = rs.getInt("id");
                List<Genre> genres = getGenresByMovieId(movieId);

                Movie movie = new Movie(
                        movieId,
                        rs.getString("title"),
                        FXCollections.observableArrayList(genres),
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

    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies (title, duration, release_date, description, images) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getDuration());
            stmt.setString(3, entity.getReleaseDate());
            stmt.setString(4, entity.getDescription());
            stmt.setString(5, entity.getImage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding movie failed, no rows affected.");
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
        String sql = "UPDATE movies SET title = ?, duration = ?, release_date = ?, description = ?, images = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getDuration());
            stmt.setString(3, entity.getReleaseDate());
            stmt.setString(4, entity.getDescription());
            stmt.setString(5, entity.getImage());
            stmt.setLong(6, entity.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating movie failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        // Bước 1: Xóa các bản ghi liên quan trong bảng movie_movie_genres
        String deleteGenresSql = "DELETE FROM movie_movie_genres WHERE movie_id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(deleteGenresSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting related movie genres: " + e.getMessage());
        }

        // Bước 2: Xóa bản ghi trong bảng movies
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting movie: " + e.getMessage());
        }
    }

    @Override
    public Movie findById(long id) {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToMovie(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT id, title, duration, release_date, description, images FROM movies";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                movies.add(mapResultSetToMovie(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Movie mapResultSetToMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getInt("id"));
        movie.setTitle(resultSet.getString("title"));
        movie.setDuration(resultSet.getString("duration"));
        movie.setReleaseDate(resultSet.getString("release_date"));
        movie.setDescription(resultSet.getString("description"));
        movie.setImage(resultSet.getString("images"));
        return movie;
    }
}