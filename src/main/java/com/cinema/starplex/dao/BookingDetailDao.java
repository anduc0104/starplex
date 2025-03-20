package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.BookingDetail;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookingDetailDao implements BaseDao<BookingDetail> {
    public Connection conn;

    public BookingDetailDao() {
        conn = DatabaseConnection.getConn();
    }

    @Override
    public void save(BookingDetail entity) {

    }

    @Override
    public boolean insert(BookingDetail entity) {
        return false;
    }

    @Override
    public void update(BookingDetail entity) {

    }

    @Override
    public void delete(long id) {

    }


    @Override
    public BookingDetail findById(long id) {
        return null;
    }

    @Override
    public List<BookingDetail> findAll() {
        return List.of();
    }

    public void createBookingDetail(int bookingId, List<Seat> seats) throws SQLException {
        String sql = "INSERT INTO booking_details (booking_id, seat_id, price) values(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Seat seat : seats) {
                SeatTypeDao seatTypeDao = new SeatTypeDao();
                double seatPrice = seatTypeDao.getPriceById(seat.getId());
                stmt.setInt(1, bookingId);
                stmt.setInt(2, seat.getId());
                stmt.setDouble(3, seatPrice);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
