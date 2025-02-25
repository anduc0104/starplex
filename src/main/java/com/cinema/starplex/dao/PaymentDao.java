package com.cinema.starplex.dao;

import com.cinema.starplex.models.Payment;

import java.util.List;

public class PaymentDao implements BaseDao<Payment>{
    @Override
    public void save(Payment entity) {

    }

    @Override
    public void update(Payment entity) {

    }

    @Override
    public void delete(Payment entity) {

    }

    @Override
    public Payment findById(long id) {
        return null;
    }

    @Override
    public List<Payment> findAll() {
        return List.of();
    }
}
