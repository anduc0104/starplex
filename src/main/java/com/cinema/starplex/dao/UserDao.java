package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDao implements BaseDao<User>{
    @Override
    public void save(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE email = :email";
            User existingUser = session.createQuery(hql, User.class)
                    .setParameter("email", user.getEmail())
                    .uniqueResult();

            if (existingUser != null) {
                throw new RuntimeException("Email already exists: " + user.getEmail());
            }
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "FROM User WHERE email = :email";
            User existingUser = session.createQuery(hql, User.class)
                    .setParameter("email", user.getEmail())
                    .uniqueResult();

            if (existingUser != null) {
                // Update existing user's fields
                existingUser.setUsername(user.getUsername());
                existingUser.setPassword(user.getPassword());
                existingUser.setPhone(user.getPhone());
                existingUser.setRole(user.getRole());

                session.merge(existingUser);
            } else {
                throw new RuntimeException("User not found with email: " + user.getEmail());
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public User findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
