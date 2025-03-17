package com.cinema.starplex.dao;

import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class RoomDao implements BaseDao<Room> {
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

                    // Lấy ký tự đầu tiên từ giá trị row
                    String rowString = resultSet.getString("s.row");
                    if (rowString != null && !rowString.isEmpty()) {
                        seat.setRow(rowString.charAt(0)); // Chuyển đổi từ String sang char
                    }

                    seat.setColNumber(resultSet.getInt("s.col_number")); // Cột ghế
                    seat.setCreatedAt(resultSet.getTimestamp("s.created_at"));

                    // Create and set the room
                    Room room = findById(roomId);
                    seat.setRoom(room);

                    // Create and set the seat type
                    SeatType seatType = new SeatType();
                    seatType.setId(resultSet.getInt("st.id"));
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

    // Lấy danh sách ghế từ database
    public Map<Integer, SeatType> getSeatTypes() {
        Map<Integer, SeatType> seatTypes = new HashMap<>();
        String query = "SELECT * FROM seat_types";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                seatTypes.put(id, new SeatType(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatTypes;
    }

    // Lấy danh sách phòng từ database
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT id, room_number, total_seats " +
                "FROM rooms " +
                "GROUP BY id, room_number " +
                "ORDER BY room_number ";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int roomNumber = resultSet.getInt("room_number");
                int totalSeats = resultSet.getInt("total_seats");
                rooms.add(new Room(id, roomNumber, totalSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Seat> getSeatsForRoom(int roomId) {
        List<Seat> seats = new ArrayList<>();
        try {
            String query = "SELECT s.id, s.row, s.col_number, s.seat_type_id, b.id as booking_id " +
                    "FROM seats s " +
                    "LEFT JOIN booking_details bd ON bd.seat_id = s.id " +
                    "LEFT JOIN bookings b ON bd.booking_id = b.id " +
                    "WHERE s.room_id = ? ORDER BY s.row, s.col_number";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String row = resultSet.getString("row");
                int colNumber = resultSet.getInt("col_number");
                int seatTypeId = resultSet.getInt("seat_type_id");
                boolean isBooked = resultSet.getObject("booking_id") != null;

                seats.add(new Seat(id, row.charAt(0), colNumber, seatTypeId, isBooked));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Room findByNumber(int roomNumber) {
        String query = "SELECT * FROM rooms WHERE room_number =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomNumber);

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
}