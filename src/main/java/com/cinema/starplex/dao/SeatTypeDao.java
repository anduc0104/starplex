package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.SeatType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SeatTypeDao implements BaseDao<SeatType>{
    @Override
    public void save(SeatType seatType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(seatType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(SeatType seatType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(seatType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(SeatType seatType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(seatType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public SeatType findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SeatType.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<SeatType> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM SeatType", SeatType.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
