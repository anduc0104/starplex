package com.cinema.starplex.dao;

import com.cinema.starplex.models.Seat;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao implements BaseDao<Seat> {
    static Connection connection = new DatabaseConnection().getConn();

    @Override
    public void save(Seat seat) {
        if (seat.getId() == null) {
            insert(seat);
        } else {
            update(seat);
        }
    }

    @Override
    public boolean insert(Seat seat) {
        String query = "INSERT INTO seats (room_id, seat_type_id, `row`, col_number, created_at) VALUES (?, ?, ?, ?, NOW())";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, seat.getRoom().getId());
            statement.setInt(2, seat.getSeatType().getId());
            statement.setString(3, String.valueOf(seat.getRow())); // Cập nhật row
            statement.setInt(4, seat.getCol_number()); // Cập nhật colNumber

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    seat.setId(generatedKeys.getInt(1));
                    return true;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //    Dùng batch insert (insertBatch) để thêm nhiều ghế một lúc, tránh gọi insert(Seat seat) nhiều lần.
//    Dùng transaction (conn.setAutoCommit(false)) để đảm bảo tất cả các ghế được thêm vào hoặc không có cái nào được thêm nếu xảy ra lỗi.
    public boolean insertBatch(List<Seat> seats) {
        String query = "INSERT INTO seats (room_id, seat_type_id, seat_number, created_at) VALUES (?, ?, ?, NOW())";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            connection.setAutoCommit(false); // Bắt đầu transaction

            for (Seat seat : seats) {
                pstmt.setInt(1, seat.getRoom().getId());
                pstmt.setInt(2, seat.getSeatType().getId());
                pstmt.setString(3, seat.getSeatNumber());
                pstmt.addBatch();
            }

            int[] result = pstmt.executeBatch();
            connection.commit(); // Commit nếu tất cả thành công


            return result.length == seats.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void update(Seat seat) {
        String query = "UPDATE seats SET room_id=?, seat_type_id=?, `row`= ?, col_number = ? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, seat.getRoom().getId());
            statement.setInt(2, seat.getSeatType().getId());
            statement.setString(3, String.valueOf(seat.getRow()));
            statement.setInt(4, seat.getCol_number());
            statement.setInt(5, seat.getId());

            statement.executeUpdate();
            // Commit nếu tất cả thành công

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM seats WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Seat findById(long id) {
        String query = "SELECT * FROM seats WHERE id = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSeat(resultSet);
                }
            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public List<Seat> findAll() {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT * FROM seats";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    seats.add(mapResultSetToSeat(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    private Seat mapResultSetToSeat(ResultSet resultSet) throws SQLException {
        Seat seat = new Seat();
        seat.setId(resultSet.getInt("id"));
        seat.setRoom(new RoomDao().findById(resultSet.getInt("room_id")));
        seat.setSeatType(new SeatTypeDao().findById(resultSet.getInt("seat_type_id")));
        seat.setRow(resultSet.getString("row")); // Cập nhật row
        seat.setCol_number(resultSet.getInt("col_number")); // Cập nhật colNumber
        return seat;
    }

    public ObservableList<Seat> getSeatsByRoomId(int roomId) {
        ObservableList<Seat> seats = FXCollections.observableArrayList();
        String query = "SELECT s.id, s.seat_number, s.seat_type_id, b.id as booking_id " +
                "FROM seats s " +
                "LEFT JOIN booking_details bd ON bd.seat_id = s.id " +
                "LEFT JOIN bookings b ON b.id = bd.booking_id" +
                "WHERE s.room_id = ? " +
                "ORDER BY s.seat_number";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String row = rs.getString("row");
                int seatTypeId = rs.getInt("seat_type_id");
                boolean isBooked = rs.getObject("booking_id") != null;

                int colNum = rs.getInt("col_number");

                seats.add(new Seat(id, row, colNum, seatTypeId, isBooked));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
}
