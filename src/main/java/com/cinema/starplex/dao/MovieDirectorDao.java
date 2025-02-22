package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.MovieDirector;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieDirectorDao implements BaseDao<MovieDirector>{
    @Override
    public void save(MovieDirector movieDirector) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(movieDirector);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MovieDirector movieDirector) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(movieDirector);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MovieDirector movieDirector) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(movieDirector);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MovieDirector findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MovieDirector.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MovieDirector> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM MovieDirector", MovieDirector.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
