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
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
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

    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        // Save a new user
        User user = new User(1, "John Doe", "john.doe@example.com", "password123", "0999999999", "admin");
        userDao.save(user);

        // Update user details
//        user.setEmail("john.doe@new.example.com");
//        userDao.update(user);
//
//        // Delete user
//        userDao.delete(user);
//
//        // Retrieve user by ID
//        User retrievedUser = userDao.findById(user.getId());
//        if (retrievedUser!= null) {
//            System.out.println("Retrieved user: " + retrievedUser.getName());
//        }

        // Retrieve all users
//        List<User> users = userDao.findAll();
    }
}
