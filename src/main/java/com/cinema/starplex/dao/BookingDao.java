package com.cinema.starplex.dao;

import com.cinema.starplex.models.*;
import com.cinema.starplex.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao implements BaseDao<Booking>{
    private Connection connection;

    public BookingDao() {
        this.connection = DatabaseConnection.getConn();
    }

    public BookingDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
        public void save(Booking booking) {
            if (booking.getId() == null) {
                insert(booking);
            } else {
                update(booking);
            }
    }

    @Override
    public boolean insert(Booking booking) {
        String query = "INSERT INTO bookings (user_id, showtime_id, total_price, status, created_at) VALUES (?, ?, ?, ?, NOW())";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getShowtime().getId());
            statement.setBigDecimal(3, booking.getTotalPrice());
            statement.setString(4, booking.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try(ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    booking.setId(generateKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Booking booking) {
        String query = "UPDATE bookings SET user_id=?, showtime_id=?, total_price=?, status=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getShowtime().getId());
            statement.setBigDecimal(3, booking.getTotalPrice());
            statement.setString(4, booking.getStatus());
            statement.setInt(5, booking.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM bookings WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking findById(long id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBooking(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
        SELECT b.*, 
               u.username, 
               s.id AS showtime_id, s.show_time, s.show_date, 
               m.id AS movie_id, m.title AS movie_title, 
               r.id AS room_id, r.room_number 
        FROM bookings b
        LEFT JOIN users u ON b.user_id = u.id
        LEFT JOIN showtimes s ON b.showtime_id = s.id
        LEFT JOIN movies m ON s.movie_id = m.id
        LEFT JOIN rooms r ON s.room_id = r.id
    """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTotalPrice(rs.getBigDecimal("total_price"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));

                // Lấy dữ liệu User
                User user = new User();
                user.setUsername(rs.getString("username"));
                booking.setUser(user);

                // Kiểm tra nếu có dữ liệu showtime
                if (rs.getObject("showtime_id") != null) {
                    Showtime showtime = new Showtime();
                    showtime.setId(rs.getInt("showtime_id"));
                    showtime.setShowTime(rs.getTime("show_time"));
                    showtime.setShowDate(rs.getDate("show_date"));

                    // Lấy thông tin Movie
                    if (rs.getObject("movie_id") != null) {
                        Movie movie = new Movie();
                        movie.setId(rs.getInt("movie_id"));
                        movie.setTitle(rs.getString("movie_title"));
                        showtime.setMovie(movie);
                    }

                    // Lấy thông tin Room
                    if (rs.getObject("room_id") != null) {
                        Room room = new Room();
                        room.setId(rs.getInt("room_id"));
                        room.setRoomNumber(rs.getInt("room_number"));
                        showtime.setRoom(room);
                    }

                    booking.setShowtime(showtime);
                }

                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


    public List<Booking> findBookingsByTotalPrice(BigDecimal total_price) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE total_price =?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, total_price);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public List<Booking> findBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE status =?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public List<Booking> findBookingsByUserId(long user_id) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.* FROM bookings b" +
                "JOIN users u ON u.id = b.user_id" +
                " WHERE u.id =?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user_id);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public List<Booking> findBookingsByShowtimeId(long showtimeId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, s.id as showtime_id, s.movie_id, s.show_time, s.show_date "
                + "FROM bookings b "
                + "LEFT JOIN showtimes s ON b.showtime_id = s.id";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTotalPrice(rs.getBigDecimal("total_price"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));

                // Lấy dữ liệu Showtime
                Showtime showtime = new Showtime();
                showtime.setId(rs.getInt("showtime_id"));
                showtime.setMovieId(rs.getInt("movie_id"));
                showtime.setShowTime(rs.getTime("show_time"));
                showtime.setShowDate(rs.getDate("show_date"));

                booking.setShowtime(showtime);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet resultSet) throws SQLException{
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        booking.setUser(new UserDao().findById(resultSet.getInt("user_id")));
        booking.setShowtime(new ShowTimeDao().findById(resultSet.getInt("showtime_id")));
        booking.setTotalPrice(resultSet.getBigDecimal("total_price"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(resultSet.getTimestamp("created_at"));
        return booking;
    }
}
