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
import java.util.ArrayList;
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
            while (rs.next()) {
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))
                ));
            }
        }
        System.out.println("Genres loaded: " + genres.size());
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
        if (entity.getId() == 0) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public boolean insert(Genre entity) {
        String sql = "INSERT INTO movie_genres (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            return pstmt.executeUpdate() > 0; // Trả về true nếu chèn thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(Genre entity) {
        String sql = "UPDATE movie_genres SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setInt(2, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM movie_genres WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Genre findById(long id) {
        String sql = "SELECT * FROM movie_genres WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Genre(
                            new SimpleIntegerProperty(rs.getInt("id")),
                            new SimpleStringProperty(rs.getString("name"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM movie_genres";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                genres.add(new Genre(
                        new SimpleIntegerProperty(rs.getInt("id")),
                        new SimpleStringProperty(rs.getString("name"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
}