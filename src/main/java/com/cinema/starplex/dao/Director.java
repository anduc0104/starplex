package com.cinema.starplex.dao;

import java.util.List;

public class Director implements BaseDao<Director>{
    @Override
    public void save(Director entity) {

    }

    @Override
    public boolean insert(Director entity) {
        return false;
    }

    @Override
    public void update(Director entity) {

    }

    @Override
    public void delete(Director entity) {

    }

    @Override
    public Director findById(long id) {
        return null;
    }

    @Override
    public List<Director> findAll() {
        return List.of();
    }
}
