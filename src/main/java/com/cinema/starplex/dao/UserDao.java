package com.cinema.starplex.dao;

import com.cinema.starplex.models.User;
import com.cinema.starplex.util.DatabaseConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        String sql = "Select * from users where username = ? and password = ?";
        Connection conn = DatabaseConnection.getConn();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId((int) rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
                DatabaseConnection.close(conn);
                return user;
            }
        }
    }

}
