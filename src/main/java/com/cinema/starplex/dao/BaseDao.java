package com.cinema.starplex.dao;

import java.util.List;

public interface BaseDao<T> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(long id);
    List<T> findAll();
}
