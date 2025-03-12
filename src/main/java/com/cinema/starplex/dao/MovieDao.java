package com.cinema.starplex.dao;

import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class MovieDao implements BaseDao<Movie>{
    private Connection connection;

    public MovieDao() {
        this.connection = DatabaseConnection.getConn();
    }

    public MovieDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
    @Override
    public void save(Movie movie) {
        if (movie.getId() == null) {
            insert(movie);
        } else {
            update(movie);
        }
    }

    @Override
    public boolean insert(Movie movie) {
        String query = "INSERT INTO movies (title, duration, release_date, rating, description, created_at) VALUES (?,?, ?, ?, ?, NOW())";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, movie.getTitle());
            statement.setFloat(2, movie.getDuration());
            statement.setLong(3, movie.getReleaseDate().getTime());
            statement.setString(4, movie.getRating());
            statement.setString(5, movie.getDescription());
            statement.setLong(6, movie.getCreatedAt().getTime());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try(ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    movie.setId(generateKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Movie movie) {
        String query = "UPDATE bookings SET title=?, duration=?, release_date=?, rating=?, description=?, created_at=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, movie.getTitle());
            statement.setFloat(2, movie.getDuration());
            statement.setLong(3, movie.getReleaseDate().getTime());
            statement.setString(4, movie.getRating());
            statement.setString(5, movie.getDescription());
            statement.setLong(6, movie.getCreatedAt().getTime());
            statement.setInt(7, movie.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Movie movie) {
        String query = "DELETE FROM movies WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, movie.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Movie deleted: " + movie.getTitle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Movie findById(long id) {
        String query = "SELECT m.* " +
                "FROM movies m " +
                "WHERE m.id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMovie(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Movie> findAll() {
        return List.of();
    }

    private Movie mapResultSetToMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getInt("id"));
        movie.setTitle(resultSet.getString("title"));
        movie.setDuration(resultSet.getFloat("duration"));
        movie.setReleaseDate(resultSet.getDate("release_date")); // ✅ Đúng
        movie.setRating(resultSet.getString("rating"));
        movie.setDescription(resultSet.getString("description"));
        movie.setCreatedAt(resultSet.getTimestamp("created_at"));
        return movie;
    }

    public ObservableList<Movie> getMovies() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String query = "SELECT m.* " +
                "FROM movies m " +
                "ORDER BY m.release_date DESC";
        try(Statement statement = connection.createStatement()) {
            try(ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    movies.add(mapResultSetToMovie(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
