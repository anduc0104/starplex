package com.cinema.starplex.dao;

import com.cinema.starplex.models.Showtime;

import java.util.List;

public class ShowtimeDao implements BaseDao<Showtime>{

    @Override
    public void save(Showtime entity) {

    }

    @Override
    public boolean insert(Showtime entity) {
        return false;
    }

    @Override
    public void update(Showtime entity) {

    }

    @Override
    public void delete(Showtime entity) {

    }

    @Override
    public Showtime findById(long id) {
        return null;
    }

    @Override
    public List<Showtime> findAll() {
        return List.of();
    }
}
