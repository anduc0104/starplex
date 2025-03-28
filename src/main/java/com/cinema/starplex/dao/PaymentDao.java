package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Payment;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao implements BaseDao<Payment>{
    static Connection connection = new DatabaseConnection().getConn();

    @Override
    public void save(Payment payment) {
        if (payment.getId() == null) {
            insert(payment);
        } else {
            update(payment);
        }
    }

    @Override
    public boolean insert(Payment payment) {
        String query = "INSERT INTO payments (booking_id, amount, payment_method, status, created_at) VALUES (?, ?, ?, ?, NOW())";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, payment.getBooking().getId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try(ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    payment.setId(generateKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Payment payment) {
        String query = "UPDATE payments SET booking_id=?, amount=?, payment_method=?, status=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, payment.getBooking().getId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getStatus());
            statement.setInt(5, payment.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM payments WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Payment findById(long id) {
        String query = "SELECT * FROM payments WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPayment(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments";

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                payments.add(mapResultSetToPayment(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private Payment mapResultSetToPayment(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setId(resultSet.getInt("id"));
        payment.setBooking(new BookingDao().findById(resultSet.getInt("booking_id")));
        payment.setAmount(resultSet.getBigDecimal("amount"));
        payment.setPaymentMethod(resultSet.getString("payment_method"));
        payment.setStatus(resultSet.getString("status"));
        payment.setCreatedAt(resultSet.getTimestamp("created_at"));
        return payment;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Booking booking = new BookingDao().findById(resultSet.getInt("id"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public void creaetePaymentDetails(int bookingId, double amount) {
        String query = "INSERT INTO payments (booking_id, amount, payment_method, status) VALUES (?,?,'Cash','Completed')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            statement.setDouble(2, amount);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}