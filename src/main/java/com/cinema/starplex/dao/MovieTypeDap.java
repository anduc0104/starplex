package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.MovieType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieTypeDap implements BaseDao<MovieType>{
    @Override
    public void save(MovieType movieType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(movieType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MovieType movieType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(movieType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MovieType movieType) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(movieType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MovieType findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MovieType.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<MovieType> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM MovieType", MovieType.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
