package com.cinema.starplex.dao;

import com.cinema.starplex.models.SeatType;

import java.util.List;

public class SeatTypeDao implements BaseDao<SeatType>{
    @Override
    public void save(SeatType entity) {

    }

    @Override
    public boolean insert(SeatType entity) {
        return false;
    }

    @Override
    public void update(SeatType entity) {

    }

    @Override
    public void delete(SeatType entity) {

    }

    @Override
    public SeatType findById(long id) {
        return null;
    }

    @Override
    public List<SeatType> findAll() {
        return List.of();
    }
}
