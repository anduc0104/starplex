package com.cinema.starplex.dao;

import java.util.List;

public class DirectorDao implements BaseDao<DirectorDao>{
    @Override
    public void save(DirectorDao entity) {

    }

    @Override
    public boolean insert(DirectorDao entity) {
        return false;
    }

    @Override
    public void update(DirectorDao entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public DirectorDao findById(long id) {
        return null;
    }

    @Override
    public List<DirectorDao> findAll() {
        return List.of();
    }
}
