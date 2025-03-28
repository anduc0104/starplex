package com.cinema.starplex.dao;

import com.cinema.starplex.models.Genre;
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

public class GenreDao implements BaseDao<Genre> {
    static Connection conn = new DatabaseConnection().getConn();

    public ObservableList<Genre> getGenres() throws SQLException {
        ObservableList<Genre> genres = FXCollections.observableArrayList();
        String sql = "SElECT * FROM movie_genres";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()){
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))
                ));
            }
        }
        return genres;
    }

    public ObservableList<String> getGenresByMovieId(int movieId) throws SQLException {
        ObservableList<String> genreNames = FXCollections.observableArrayList();
        String sql = """
        SELECT g.name 
        FROM movie_genres g
        JOIN movie_movie_genres mg ON g.id = mg.genre_id
        WHERE mg.movie_id = ?
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    genreNames.add(rs.getString("name"));
                }
            }
        }
        return genreNames;
    }



    @Override
    public void save(Genre entity) {

    }

    @Override
    public boolean insert(Genre entity) {
        return false;
    }

    @Override
    public void update(Genre entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Genre findById(long id) {
        return null;
    }

    @Override
    public List<Genre> findAll() {
        return List.of();
    }
}