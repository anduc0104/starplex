package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Genre;

import java.util.List;

public interface BaseDao<T> {
    void save(T entity);
    boolean insert(T entity);
    void update(T entity);
    void delete(long id);
    T findById(long id);
    List<T> findAll();
}
