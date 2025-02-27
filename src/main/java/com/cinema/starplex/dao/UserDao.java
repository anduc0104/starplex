package com.cinema.starplex.dao;

import com.cinema.starplex.models.User;
import com.cinema.starplex.util.DatabaseConnection;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements BaseDao<User> {
    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User findById(long id) {

        return null;
    }

    @Override
    public List<User> findAll() {

        return List.of();
    }

    public User login(String username, String password) throws SQLException {
        User user = null;
        String sql = "Select * from users where username = ?";
        Connection conn = DatabaseConnection.getConn();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    if (BCrypt.checkpw(password, hashedPassword)) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(hashedPassword);
                        user.setEmail(rs.getString("email"));
                        return user;
                    }
                }
                DatabaseConnection.close(conn);
                return null;
            }
        }
    }

}
