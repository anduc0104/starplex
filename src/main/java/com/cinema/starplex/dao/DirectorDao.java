package com.cinema.starplex.dao;
import com.cinema.starplex.models.Director;
import java.util.List;

public class DirectorDao implements BaseDao<Director>{
    @Override
    public void save(Director director) {

    }

    @Override
    public boolean insert(Director entity) {
        return false;
    }

    @Override
    public void update(Director director) {

    }

    @Override
    public void delete(Director director) {

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
