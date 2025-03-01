package com.cinema.starplex.dao;

import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MovieDao implements BaseDao<Movie> {

    private final Connection conn;

    public MovieDao() {
        conn = DatabaseConnection.getConn();
    }

    public ObservableList<Movie> getMovies() throws SQLException {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String sql = "SELECT * FROM movies";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("title")),
                        new SimpleStringProperty(rs.getString("directors")),
                        new SimpleStringProperty(rs.getString("actors")),
                        new SimpleStringProperty(rs.getString("genres")),
                        new SimpleStringProperty(rs.getString("duration")),
                        new SimpleStringProperty(rs.getString("release_date")),
                        new SimpleStringProperty(rs.getString("description")),
                        new SimpleStringProperty(rs.getString("images"))
                ));
            }
        }
        return movies;
    }


    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies (title, director, actors, genre, duration, release_date, description, image) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getDirector());
            pstmt.setString(3, entity.getActors());
            pstmt.setString(4, entity.getGenre());
            pstmt.setString(5, entity.getDuration());
            pstmt.setString(6, entity.getReleaseDate());
            pstmt.setString(7, entity.getDescription());
            pstmt.setString(8, entity.getImage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movies SET title =?, director =?, actors =?, genre =?, duration =?, release_date =?, description =?, image =? WHERE id =?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getDirector());
            pstmt.setString(3, entity.getActors());
            pstmt.setString(4, entity.getGenre());
            pstmt.setString(5, entity.getDuration());
            pstmt.setString(6, entity.getReleaseDate());
            pstmt.setString(7, entity.getDescription());
            pstmt.setString(8, entity.getImage());
            pstmt.setInt(9, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Movie entity) {
        String sql = "DELETE FROM movies WHERE id =?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Movie findById(long id) {
        return null;
    }

    @Override
    public List<Movie> findAll() {
        return List.of();
    }
}
