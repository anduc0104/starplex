package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.BookingDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BookingDetailDao implements BaseDao<BookingDetail>{
    @Override
    public void save(BookingDetail bookingDetail) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookingDetail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(BookingDetail bookingDetail) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(bookingDetail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(BookingDetail bookingDetail) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(bookingDetail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public BookingDetail findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BookingDetail.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<BookingDetail> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM BookingDetail", BookingDetail.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
