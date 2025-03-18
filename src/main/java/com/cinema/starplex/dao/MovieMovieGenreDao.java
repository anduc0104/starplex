package com.cinema.starplex.dao;

import com.cinema.starplex.models.MovieGenre;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieMovieGenreDao implements BaseDao<MovieGenre> {

    @Override
    public void save(MovieGenre entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public boolean insert(MovieGenre entity) {
        String sql = "INSERT INTO movie_genres (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            return stmt.executeUpdate() > 0; // Trả về true nếu chèn thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(MovieGenre entity) {
        String sql = "UPDATE movie_genres SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        // Bước 1: Xóa các bản ghi liên quan trong bảng movie_movie_genres
        String deleteGenresSql = "DELETE FROM movie_movie_genres WHERE genre_id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(deleteGenresSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting related movie genres: " + e.getMessage());
        }

        // Bước 2: Xóa bản ghi trong bảng genres (nếu cần)
        String sql = "DELETE FROM genres WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting genre: " + e.getMessage());
        }
    }

    @Override
    public MovieGenre findById(long id) {
        String sql = "SELECT * FROM movie_genres WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id); // Sử dụng setLong để thiết lập ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Sử dụng getObject() để lấy giá trị dưới dạng Integer
                Integer genreId = (Integer) rs.getObject("id");
                String name = rs.getString("name");
                return new MovieGenre(genreId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy
    }

    @Override
    public List<MovieGenre> findAll() {
        List<MovieGenre> genres = new ArrayList<>();
        String sql = "SELECT * FROM movie_genres";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genres.add(new MovieGenre(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
}