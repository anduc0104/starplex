package com.cinema.starplex.dao;

import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatTypeDao implements BaseDao<SeatType> {

    @Override
    public void save(SeatType seatType) {
        String sql = "INSERT INTO seat_type (name, price) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, seatType.getName());
            stmt.setBigDecimal(2, seatType.getPrice());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Add failed seat type, no lines are affected.");
            }

            // Lấy ID tự động tạo
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                seatType.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding chair type: ", e);
        }
    }

    @Override
    public boolean insert(SeatType entity) {
        return false;
    }

    @Override
    public void update(SeatType seatType) {
        if (!existsById(seatType.getId())) {
            System.err.println("ID Seat Type " + seatType.getId() + " does not exist, cannot be updated.");
            return;
        }

        String sql = "UPDATE seat_type SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, seatType.getName());
            stmt.setBigDecimal(2, seatType.getPrice());
            stmt.setInt(3, seatType.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("Update failed, no lines were affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating seat type: ", e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM seat_type WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("Delete failed, no lines were affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when deleting chair type: ", e);
        }
    }

    @Override
    public SeatType findById(long id) {
        String sql = "SELECT * FROM seat_type WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SeatType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when searching for chair type by ID: ", e);
        }
        return null;
    }

    @Override
    public List<SeatType> findAll() {
        List<SeatType> seatTypes = new ArrayList<>();
        String sql = "SELECT * FROM seat_type ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seatTypes.add(new SeatType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting seat type list: ", e);
        }
        return seatTypes;
    }

    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM seat_type WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking ID exists: ", e);
        }
        return false;
    }

    public SeatType findByName(String string) {
        String sql = "SELECT * FROM seat_type WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, string);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SeatType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when searching for chair type by name: ", e);
        }
        return null;
    }
}