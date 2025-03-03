package com.cinema.starplex.dao;

import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

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
    public void save(Movie entity) {

    }

    @Override
    public boolean insert(Movie entity) {
        return false;
    }

    @Override
    public void update(Movie entity) {

    }

    @Override
    public void delete(Movie entity) {

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
}
