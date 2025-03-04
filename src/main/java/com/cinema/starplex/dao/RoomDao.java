package com.cinema.starplex.dao;

import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.DatabaseConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDao implements BaseDao<Room>{
    private Connection connection;

    public RoomDao() {
        this.connection = DatabaseConnection.getConn();
    }

    public RoomDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void save(Room room) {
        if (room.getId() == null) {
            insert(room);
        } else {
            update(room);
        }
    }

    @Override
    public boolean insert(Room room) {
        String query = "INSERT INTO rooms (room_number, total_seats, created_at) VALUES (?, ?, NOW())";
        System.out.println(room.getRoomNumber());
        System.out.println(room.getTotalSeats());

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getTotalSeats());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Room room) {
        String query = "UPDATE rooms SET room_number = ?, total_seats = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getTotalSeats());
            statement.setInt(3, room.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Room findById(long id) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToRoom(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return null;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                rooms.add(mapResultSetToRoom(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Room> findRoomByShowtimeId(long showtimeId) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.* FROM rooms r " +
                "JOIN showtimes s ON r.id = s.room_id " +
                "WHERE s.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, showtimeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.add(mapResultSetToRoom(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Seat> findSeatsByRoomId(long roomId) {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT s.*, st.* FROM seats s " +
                "JOIN seat_types st ON s.seat_type_id = st.id " +
                "WHERE s.room_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Seat seat = new Seat();
                    seat.setId(resultSet.getInt("s.id"));
                    seat.setSeatNumber(resultSet.getString("s.seat_number"));
                    seat.setCreatedAt(resultSet.getTimestamp("s.created_at"));

                    // Create and set the room
                    Room room = findById(roomId);
                    seat.setRoom(room);

                    // Create and set the seat type
                    SeatType seatType = new SeatType();
                    seatType.setId(resultSet.getInt("st.id"));
                    // Set other seat type properties as needed
                    seat.setSeatType(seatType);

                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("id"));
        room.setRoomNumber(resultSet.getInt("room_number"));
        room.setTotalSeats(resultSet.getInt("total_seats"));
        room.setCreatedAt(resultSet.getTimestamp("created_at"));
        return room;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
