package com.cinema.starplex.dao;

import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowTimeDao implements BaseDao<Showtime>{

    @Override
    public void save(Showtime entity) {

    }

    @Override
    public boolean insert(Showtime entity) {
        return false;
    }

    @Override
    public void update(Showtime entity) {

    }

    @Override
    public void delete(long id) {

    }


    @Override
    public Showtime findById(long id) {
        return null;
    }

    @Override
    public List<Showtime> findAll() {
        return List.of();
    }




    private Connection conn;

    public ShowTimeDao() {
        this.conn = DatabaseConnection.getConn();
    }

    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(new Showtime(
                        rs.getInt("id"),
                        null,
                        null,
                        rs.getTimestamp("start_time"),
                        rs.getBigDecimal("price"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public boolean insertShowtime(Showtime showtime) {
        String sql = "INSERT INTO showtimes (movie_id, room_id, start_time, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (showtime.getMovie() != null) {
                stmt.setInt(1, showtime.getMovie().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            if (showtime.getRoom() != null) {
                stmt.setInt(2, showtime.getRoom().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setTimestamp(3, showtime.getStartTime());
            stmt.setBigDecimal(4, showtime.getPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE showtimes SET movie_id = ?, room_id = ?, start_time = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovie().getId());
            stmt.setInt(2, showtime.getRoom().getId());
            stmt.setTimestamp(3, showtime.getStartTime());
            stmt.setBigDecimal(4, showtime.getPrice());
            stmt.setInt(5, showtime.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
}