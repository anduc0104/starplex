package com.cinema.starplex.dao;

import com.cinema.starplex.models.MovieGenre;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MovieMovieGenreDao implements BaseDao<MovieGenre> {
    @Override
    public void save(MovieGenre entity) {

    }

    @Override
    public boolean insert(MovieGenre entity) {
        return false;
    }

    @Override
    public void update(MovieGenre entity) {

    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM movie_movie_genres WHERE movie_id = ?";
        // execute the SQL query
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting chair: " + e.getMessage());
        }

    }

    @Override
    public MovieGenre findById(long id) {
        return null;
    }

    @Override
    public List<MovieGenre> findAll() {
        return List.of();
    }

    public List<MovieGenre> getGenresByMovieId(int id) {
        List<MovieGenre> genres = List.of();
        String sql = "SELECT mg.*" +
                "                FROM movies m" +
                "                JOIN movie_types mt ON mt.movie_id = m.id " +
                "                JOIN movie_genres mg ON mt.genre_id = mg.id " +
                "                 WHERE mt.movie_id = ?;";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                genres.add(new MovieGenre(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("Error when getting genres for movie: " + e.getMessage());
        }

        return genres;
    }
}