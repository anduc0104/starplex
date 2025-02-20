package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.Booking;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BookingDao implements BaseDao<Booking>{
    @Override
    public void save(Booking booking) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Booking booking) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Booking booking) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Booking findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Booking.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<Booking> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Booking", Booking.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
