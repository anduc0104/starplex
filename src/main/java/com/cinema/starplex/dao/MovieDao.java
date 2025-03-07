package com.cinema.starplex.dao;

import com.cinema.starplex.models.Genre;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                int movieId = rs.getInt("id");
                List<Genre> genres = getGenresByMovieId(movieId);

                movies.add(new Movie(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("title")),
                        new SimpleStringProperty(rs.getString("directors")),
                        new SimpleStringProperty(rs.getString("actors")),
                        new SimpleListProperty<>(FXCollections.observableArrayList(genres)),
                        new SimpleStringProperty(rs.getString("duration")),
                        new SimpleStringProperty(rs.getString("release_date")),
                        new SimpleStringProperty(rs.getString("description")),
                        new SimpleStringProperty(rs.getString("images"))
                ));
            }
        }
        System.out.println("Movies loaded: " + movies.size()); // Kiểm tra số lượng dữ liệu
        return movies;
    }

    public List<Genre> getGenresByMovieId(int movieId) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.id, g.name FROM movie_genres g " +
                "JOIN movie_type mt ON g.id = mt.genre_id " +
                "WHERE mt.movie_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Chuyển đổi int và String sang IntegerProperty và StringProperty
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))
                ));
            }
        }
        return genres;
    }


    @Override
    public void save(Movie entity) {}


    @Override
    public void update(Movie entity) {
    }

    @Override
    public void delete(Movie entity) {
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
