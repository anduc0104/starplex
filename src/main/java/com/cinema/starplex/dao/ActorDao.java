package com.cinema.starplex.dao;
import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.Actor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ActorDao implements BaseDao<Actor>{
    @Override
    public void save(Actor actor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(actor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Actor actor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(actor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Actor actor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(actor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Actor findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Actor.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Actor> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Actor", Actor.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}