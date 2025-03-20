package com.cinema.starplex.dao;

import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowTimeDao implements BaseDao<Showtime> {
    private Connection conn;

    public ShowTimeDao() {
        this.conn = DatabaseConnection.getConn();
    }

    @Override
    public void save(Showtime entity) {
        insertShowtime(entity);
    }

    @Override
    public boolean insert(Showtime entity) {
        return insertShowtime(entity);
    }

    @Override
    public void update(Showtime entity) {
        updateShowtime(entity);
    }

    @Override
    public void delete(long id) {
        deleteShowtime((int) id);
    }

    @Override
    public Showtime findById(long id) {
        return getShowtimeById((int) id);
    }

    @Override
    public List<Showtime> findAll() {
        return getAllShowtimes();
    }

    /**
     * Lấy danh sách tất cả lịch chiếu
     */
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.id, s.movie_id, m.title AS movie_title, s.room_id, r.room_number, s.show_date, s.show_time, s.price, s.created_at " +
                "FROM showtimes s " +
                "LEFT JOIN movies m ON s.movie_id = m.id " +
                "LEFT JOIN rooms r ON s.room_id = r.id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(new Showtime(
                        rs.getInt("id"),
                        new Movie(rs.getInt("movie_id"), rs.getString("movie_title")),
                        new Room(rs.getInt("room_id"), rs.getString("room_number")),
                        rs.getDate("show_date"),
                        rs.getTime("show_time"),
                        rs.getBigDecimal("price"),
                        rs.getDate("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy một lịch chiếu theo ID
     */
    public Showtime getShowtimeById(int id) {
        String sql = "SELECT s.id, s.movie_id, m.title AS movie_title, s.room_id, r.room_number, s.show_date, s.show_time, s.price, s.created_at " +
                "FROM showtimes s " +
                "LEFT JOIN movies m ON s.movie_id = m.id " +
                "LEFT JOIN rooms r ON s.room_id = r.id " +
                "WHERE s.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Showtime(
                        rs.getInt("id"),
                        new Movie(rs.getInt("movie_id"), rs.getString("movie_title")),
                        new Room(rs.getInt("room_id"), rs.getString("room_number")),
                        rs.getDate("show_date"),
                        rs.getTime("show_time"),
                        rs.getBigDecimal("price"),
                        rs.getDate("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Chèn một lịch chiếu mới
     */
    public boolean insertShowtime(Showtime showtime) {
        String sql = "INSERT INTO showtimes (movie_id, room_id, show_date, show_time, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovie().getId());
            stmt.setInt(2, showtime.getRoom().getId());
            stmt.setDate(3, showtime.getShowDate());
            stmt.setTime(4, showtime.getShowTime());
            stmt.setBigDecimal(5, showtime.getPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật lịch chiếu
     */
    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE showtimes SET movie_id = ?, room_id = ?, show_date = ?, show_time = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovie().getId());
            stmt.setInt(2, showtime.getRoom().getId());
            stmt.setDate(3, showtime.getShowDate());
            stmt.setTime(4, showtime.getShowTime());
            stmt.setBigDecimal(5, showtime.getPrice());
            stmt.setInt(6, showtime.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa lịch chiếu
     */
    public boolean deleteShowtime(int id) {
        String sql = "DELETE FROM showtimes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy số phòng theo ID
     */
    public String getRoomNumber(int roomId) {
        String sql = "SELECT room_number FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("room_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}