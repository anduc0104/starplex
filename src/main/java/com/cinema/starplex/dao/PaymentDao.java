package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentDao implements BaseDao<Payment>{
    @Override
    public void save(Payment payment) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(payment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Payment payment) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(payment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Payment payment) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(payment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Payment findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Payment.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<Payment> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Payment", Payment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
