package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.MovieGenre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieGenreDao implements BaseDao<MovieGenre>{
    @Override
    public void save(MovieGenre movieGenre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(movieGenre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MovieGenre movieGenre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(movieGenre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MovieGenre movieGenre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(movieGenre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MovieGenre findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MovieGenre.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MovieGenre> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM MovieGenre", MovieGenre.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
