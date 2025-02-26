package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private UserDao userDao;

    // Store active sessions in memory
    private Map<String, Map<String, Object>> sessions = new HashMap<>();

    public UserController(Connection connection) {
        this.userDao = new UserDao(connection);
    }

    public User getUserById(long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public boolean createUser(String username, String email, String password, String phone, String role) {
        // You might want to add password hashing here
        String hashedPassword = hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setPhone(phone);
        user.setRole(role);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return userDao.insert(user);
    }

    public void updateUser(Integer id, String username, String email, String password, String phone, String role) {
        User user = userDao.findById(id);
        if (user == null) {
            return;
        }

        user.setUsername(username);
        user.setEmail(email);

        // Only update password if a new one is provided
        if (password != null && !password.isEmpty()) {
            String hashedPassword = hashPassword(password);
            user.setPassword(hashedPassword);
        }

        user.setPhone(phone);
        user.setRole(role);

        userDao.update(user);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

   public User authenticate(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
   }

   public String login(String username, String password) {
        User user = authenticate(username, password);
        if (user != null) {
            String sessionToken = generateSessionToken();

            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("user", user);
            sessionData.put("userId", user.getId());
            sessionData.put("username", user.getUsername());
            sessionData.put("role", user.getRole());

            sessions.put(sessionToken, sessionData);
        }
        return null;
   }

   public void logout(String sessionToken) {
        sessions.remove(sessionToken);
   }

   public User getUserFromSession(String sessionToken) {
        Map<String, Object> sessionData = sessions.get(sessionToken);
        if (sessionData!= null) {
            return (User) sessionData.get("user");
        }
        return null;
   }

   public String generateSessionToken() {
        return BCrypt.gensalt() + System.currentTimeMillis();
   }

   public  String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
   }

   public boolean verifyPassword(String rawPassword, String hashedPassword) {
       return BCrypt.checkpw(rawPassword, hashedPassword);
   }
}