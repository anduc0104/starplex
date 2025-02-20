package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.Movie;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieDao implements BaseDao<Movie>{
    @Override
    public void save(Movie movie) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Movie movie) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Movie movie) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Movie findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Movie.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<Movie> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Movie", Movie.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
