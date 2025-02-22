package com.cinema.starplex.dao;
import com.cinema.starplex.models.MovieActor;
import com.cinema.starplex.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieActorDao implements BaseDao<MovieActor>{
    @Override
    public void save(MovieActor movieActor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(movieActor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MovieActor movieActor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(movieActor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MovieActor movieActor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(movieActor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MovieActor findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MovieActor.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MovieActor> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM MovieActor", MovieActor.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
