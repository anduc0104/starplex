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

    private final Connection conn;

    public GenreDao() {
        conn = DatabaseConnection.getConn();
    }

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
        System.out.println("Genres loaded: " + genres.size());
        return genres;
    }


    @Override
    public void save(Genre entity) {

    }

    @Override
    public void update(Genre entity) {

    }

    @Override
    public void delete(Genre entity) {

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
