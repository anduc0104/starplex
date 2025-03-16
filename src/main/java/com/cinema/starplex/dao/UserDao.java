package com.cinema.starplex.dao;

import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.User;
import com.cinema.starplex.models.UserFX;
import com.cinema.starplex.util.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements BaseDao<User> {
    private User user;
    private Connection conn;

    public UserDao() {
        user = new User();
        conn = DatabaseConnection.getConn();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            pstmt.setString(3, user.getFull_name()); // Sửa lại chỗ này nếu getter đúng là getFullName()
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getRole());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu COUNT(*) > 0 thì username đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean insert(User entity) {
        return false;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET full_name = ?, username = ? , email = ?, phone = ?, role = ? WHERE id = ?";
        boolean updatePassword = (user.getPassword() != null && !user.getPassword().isEmpty());

        if (updatePassword) {
            sql = "UPDATE users SET full_name = ?, username = ? , email = ?, phone = ?, role = ?, password = ? WHERE id = ?";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFull_name());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getRole());

            if (updatePassword) {
                pstmt.setString(6, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                pstmt.setInt(7, user.getId());
            } else {
                pstmt.setInt(6, user.getId());
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        System.out.println(id);
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public User findById(long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("password") // Lấy password cũ nếu không cập nhật
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try(Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("role"),
                        resultSet.getString("password") // Lấy password cu nếu không cập nhật
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User login(String username, String password) throws SQLException {
        System.out.println(username);
        System.out.println(password);
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
                        user.setRole(rs.getString("role"));
                        return user;
                    }
                }
                return null;
            }
        }
    }

    public ObservableList<UserFX> loadUsers() {
        ObservableList<UserFX> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users ";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new UserFX(
                            new SimpleIntegerProperty(rs.getInt("id")),
                            new SimpleStringProperty(rs.getString("full_name")),
                            new SimpleStringProperty(rs.getString("username")),
                            new SimpleStringProperty(rs.getString("email")),
                            new SimpleStringProperty(rs.getString("phone")),
                            new SimpleStringProperty(rs.getString("role"))

                    ));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
