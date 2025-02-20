package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SeatDao implements BaseDao<Seat>{
    @Override
    public void save(Seat seat) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Seat seat) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Seat seat) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Seat findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<Seat> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Seat", Seat.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
