package com.cinema.starplex.dao;

import com.cinema.starplex.models.Movie;

import java.util.List;

public class MovieDao implements BaseDao<Movie>{
    @Override
    public void save(Movie entity) {

    }

    @Override
    public void update(Movie entity) {

    }

    @Override
    public void delete(Movie entity) {

    }

    @Override
    public Movie findById(long id) {
        return null;
    }

    @Override
    public List<Movie> findAll() {
        return List.of();
    }
}
