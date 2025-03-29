package com.cinema.starplex.dao;

import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatTypeDao implements BaseDao<SeatType> {
    static Connection connection = new DatabaseConnection().getConn();

    public SeatTypeDao() {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void save(SeatType seatType) {
        if (seatType.getId() == null) {
            insert(seatType);
        } else {
            update(seatType);
        }
    }

    @Override
    public boolean insert(SeatType seatType) {
        String query = "INSERT INTO seat_types(name, price, created_at)VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, seatType.getName());
            statement.setBigDecimal(2, seatType.getPrice());
            statement.setTimestamp(3, seatType.getCreatedAt());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    seatType.setId(generateKeys.getInt(1));
                    return true;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(SeatType seatType) {
        String query = "UPDATE seat_types SET name = ?, price=?, created_at=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, seatType.getName());
            statement.setBigDecimal(2, seatType.getPrice());
            statement.setTimestamp(3, seatType.getCreatedAt());
            statement.setLong(4, seatType.getId());

            statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM seat_types WHERE id =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SeatType findById(long id) {
        String query = "SELECT * FROM seat_types WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSeatType(resultSet);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SeatType> findAll() {
        List<SeatType> seatTypes = new ArrayList<>();
        String query = "SELECT * " + "FROM seat_types  ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    seatTypes.add(mapResultSetToSeatType(resultSet));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatTypes;
    }

    private SeatType mapResultSetToSeatType(ResultSet resultSet) throws SQLException {
        SeatType seatType = new SeatType();
        seatType.setId(resultSet.getInt("id"));
        seatType.setName(resultSet.getString("name"));
        seatType.setPrice(resultSet.getBigDecimal("price"));
        seatType.setCreatedAt(resultSet.getTimestamp("created_at"));
        return seatType;
    }

    public String getNameById(int id) throws SQLException {
        String seatTypeName;
        String sql = "SELECT * FROM seat_types as st WHERE st.id = ?";
        // Execute the query and get the result
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        try {
            // Get the result set
            ResultSet rs = stmt.executeQuery();
            // Process the result set
            if (rs.next()) {
                seatTypeName = rs.getString("name");
            } else {
                seatTypeName = "Seat Type not found";
            }
            
            return seatTypeName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getPriceById(int seatTypeId) throws SQLException {
        String query = "SELECT price FROM seat_types WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, seatTypeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
            
        }
        return 0;
    }
}
