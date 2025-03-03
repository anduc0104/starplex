package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDao implements BaseDao<Showtime>{
    private Connection connection;

    public ShowtimeDao() {
        this.connection = DatabaseConnection.getConn();
    }

    public ShowtimeDao(Connection connection) {
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
        String query = "INSERT INTO showtimes(movie_id, room_id, start_time, price, created_at) VALUES (?,?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, showtime.getMovie().getId());
            statement.setLong(2, showtime.getRoom().getId());
            statement.setTimestamp(3, showtime.getStartTime());
            statement.setBigDecimal(4, showtime.getPrice());
            statement.setTimestamp(5, showtime.getCreatedAt());

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
        String query = "UPDATE showtimes SET movie_id=?, room_id=?, start_time=?, price=?, created_at=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, showtime.getMovie().getId());
            statement.setLong(2, showtime.getRoom().getId());
            statement.setTimestamp(3, showtime.getStartTime());
            statement.setBigDecimal(4, showtime.getPrice());
            statement.setTimestamp(5, showtime.getCreatedAt());
            statement.setLong(6, showtime.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Showtime showtime) {
        String query = "DELETE FROM showtimes WHERE id =?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, showtime.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Showtime findById(long id) {
        String query = "SELECT s.* " +
                "FROM showtimes s " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN rooms r ON s.room_id = r.id " +
                "WHERE s.id = ?";
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
        String query = "SELECT s.* " +
                "FROM showtimes s " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN rooms r ON s.room_id = r.id";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    showtimes.add(mapResultSetToShowtime(resultSet));
                }
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
