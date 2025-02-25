package com.cinema.starplex.dao;

import com.cinema.starplex.config.HibernateUtil;
import com.cinema.starplex.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.List;

public class UserDao implements BaseDao<User>{
    @Override
    public void save(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM users WHERE email = :email";
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

            String hql = "FROM users WHERE email = :email";
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

    public boolean registerUser(String username, String password, String email, String phone, String role) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User existingUser = getUserByUsername(username);
            User existingEmail = getUserByEmail(email);
            if (existingUser!= null || existingEmail!=null) {
                System.out.println("User or Email already exists");
                return false;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role != null ? role : "customer");
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            session.save(user);
            transaction.commit();
            System.out.println("User saved successfully");
            return true;
        } catch (Exception e) {
            if (transaction!= null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        Transaction transaction = null;
        User user = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<User> query = session.createQuery("FROM User WHERE username =:username", User.class);
            query.setParameter("username", username);
            user = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    public static void main(String[] args) {
//        UserDao userDao = new UserDao();
////         Save a new user
////        User user = new User(1, "John Doe", "john.doe@example.com", "password123", "0999999999", "admin", '2025-02-15 10:30:00');
////        userDao.save(user);
//
//    }
}
