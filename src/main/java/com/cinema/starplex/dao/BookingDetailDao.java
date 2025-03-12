package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.BookingDetail;

import java.util.List;

public class BookingDetailDao implements BaseDao<BookingDetail> {
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
}
