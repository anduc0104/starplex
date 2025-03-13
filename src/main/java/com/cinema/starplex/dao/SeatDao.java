package com.cinema.starplex.dao;

import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao implements BaseDao<Seat> {

    @Override
    public void save(Seat seat) {
        String sql = "INSERT INTO seats (room_id, seat_type_id, seat_number, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, seat.getRoom().getId());
            stmt.setInt(2, seat.getSeatType().getId());
            stmt.setString(3, seat.getSeatNumber());
            stmt.setTimestamp(4, seat.getCreatedAt());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Adding seats fails, no lines are affected.");
            }

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                seat.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error adding chair: " + e.getMessage());
        }
    }

    @Override
    public boolean insert(Seat entity) {
        return false;
    }

    @Override
    public void update(Seat seat) {
        String sql = "UPDATE seats SET room_id = ?, seat_type_id = ?, seat_number = ?, created_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seat.getRoom().getId());
            stmt.setInt(2, seat.getSeatType().getId());
            stmt.setString(3, seat.getSeatNumber());
            stmt.setTimestamp(4, seat.getCreatedAt());
            stmt.setInt(5, seat.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while updating seat: " + e.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM seats WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error when deleting chair: " + e.getMessage());
        }
    }

    @Override
    public Seat findById(long id) {
        String sql = "SELECT * FROM seats WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Seat(
                        rs.getInt("id"),
                        new Room(rs.getInt("room_id")),
                        new SeatType(rs.getInt("seat_type_id")),
                        rs.getString("seat_number"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error when searching for seats by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Seat> findAll() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.id, s.seat_number, s.created_at, " +
                "r.id AS room_id, r.room_number, " +
                "st.id AS seat_type_id, st.name " +
                "FROM seats s " +
                "JOIN rooms r ON s.room_id = r.id " +
                "JOIN seat_types st ON s.seat_type_id = st.id ";

        try (Connection conn = DatabaseConnection.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(rs.getInt("room_id"), rs.getInt("room_number"));
                SeatType seatType = new SeatType(rs.getInt("seat_type_id"), rs.getString("name"));
                Seat seat = new Seat(
                        rs.getInt("id"),
                        room,
                        seatType,
                        rs.getString("seat_number"),
                        rs.getTimestamp("created_at")
                );
                seats.add(seat);
            }
        } catch (SQLException e) {
            System.err.println("Error getting seat list: " + e.getMessage());
        }
        return seats;
    }
}