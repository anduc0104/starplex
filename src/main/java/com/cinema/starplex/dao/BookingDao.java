package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao implements BaseDao<Booking> {
    static Connection connection = new DatabaseConnection().getConn();

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
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getShowtime().getId());
            statement.setBigDecimal(3, booking.getTotalPrice());
            statement.setString(4, booking.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generateKeys = statement.getGeneratedKeys()) {
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
        String query = "UPDATE bookings SET user_id=?, showtime_id=?, total_price=?, status=?, total_tickets = ? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getShowtime().getId());
            statement.setBigDecimal(3, booking.getTotalPrice());
            statement.setString(4, booking.getStatus());
            statement.setInt(5, booking.getTotalTicket());
            statement.setInt(6, booking.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String deletePaymentQuery = "DELETE FROM payments WHERE booking_id = ?";
        String deleteBookingDetailQuery = "DELETE FROM booking_details WHERE booking_id = ?";
        String deleteBookingQuery = "DELETE FROM bookings WHERE id=?";
        try {
            PreparedStatement stmt1 = connection.prepareStatement(deletePaymentQuery);
            PreparedStatement stmt2 = connection.prepareStatement(deleteBookingDetailQuery);
            PreparedStatement stmt3 = connection.prepareStatement(deleteBookingQuery);

            //Xóa payment
            stmt1.setLong(1, id);
            stmt1.executeUpdate();
            // Xóa booking_detail trước
            stmt2.setLong(1, id);
            stmt2.executeUpdate();
            //xóa booking
            stmt3.setLong(1, id);
            stmt3.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking findById(long id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
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
        String query = "SELECT * FROM bookings";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                bookings.add(mapResultSetToBooking(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findBookingsByTotalPrice(BigDecimal total_price) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE total_price =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, total_price);

            try (ResultSet resultSet = statement.executeQuery()) {
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user_id);

            try (ResultSet resultSet = statement.executeQuery()) {
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
        String query = "SELECT b.* FROM bookings b " +
                "JOINS showtimes s ON s.id = b.showtime_id" +
                "WHERE s.id =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, showtimeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        booking.setUser(new UserDao().findById(resultSet.getInt("user_id")));
        booking.setShowtime(new ShowTimeDao().findById(resultSet.getInt("showtime_id")));
        booking.setTotalPrice(resultSet.getBigDecimal("total_price"));
        booking.setTotalTicket(resultSet.getInt("total_tickets"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(resultSet.getTimestamp("created_at"));
        return booking;
    }

    public int createBookingPayment(Booking booking) {
        String query = "INSERT INTO bookings (user_id, showtime_id, total_price, status, total_tickets ,created_at) VALUES (?, ?, ?, ?,?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getUserId());
            statement.setInt(2, booking.getShowtime_id());
            statement.setDouble(3, booking.getTotalPriceToPayment());
            statement.setString(4, "Confirmed");
            statement.setInt(5, booking.getTotalTicket());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    return generateKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}