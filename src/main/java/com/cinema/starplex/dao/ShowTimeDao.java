package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowTimeDao implements BaseDao<Showtime>{
    private Connection connection;

    public ShowTimeDao() {
        this.connection = DatabaseConnection.getConn();
    }

    public ShowTimeDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
    @Override
    public void save(Showtime showtime) {
        if (showtime.getId() == null) {
            insert(showtime);
        } else {
            update(showtime);
        }
    }

    @Override
    public boolean insert(Showtime showtime) {
        String query = "INSERT INTO showtimes (movie_id, room_id, start_time, price, created_at) VALUES (?, ?, ?, ?, NOW())";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, showtime.getMovie().getId());
            statement.setInt(2, showtime.getRoom().getId());
            statement.setTimestamp(3, new Timestamp(showtime.getStartTime().getTime()));
            statement.setBigDecimal(4, showtime.getPrice());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try(ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    showtime.setId(generateKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Showtime showtime) {
        String query = "UPDATE showtimes SET movie_id=?, room_id=?, start_time=?, price=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, showtime.getMovie().getId());
            statement.setInt(2, showtime.getRoom().getId());
            statement.setTimestamp(3, new Timestamp(showtime.getStartTime().getTime()));
            statement.setBigDecimal(4, showtime.getPrice());
            statement.setInt(5, showtime.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM showtimes WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Showtime findById(long id) {
        String query = "SELECT * FROM showtimes WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToShowtime(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Showtime> findAll() {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT * FROM showtimes";

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                showtimes.add(mapResultSetToShowtime(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    private Showtime mapResultSetToShowtime(ResultSet resultSet) throws SQLException{
        Showtime showtime = new Showtime();
        showtime.setId(resultSet.getInt("id"));
        showtime.setMovie(new MovieDao().findById(resultSet.getInt("movie_id")));
        showtime.setRoom(new RoomDao().findById(resultSet.getInt("room_id")));
        showtime.setStartTime(resultSet.getTimestamp("start_time"));
        showtime.setPrice(resultSet.getBigDecimal("price"));
        showtime.setCreatedAt(resultSet.getTimestamp("created_at"));
        return showtime;
    }
}
