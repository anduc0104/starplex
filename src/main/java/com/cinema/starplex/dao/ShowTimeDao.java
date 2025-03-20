package com.cinema.starplex.dao;

import com.cinema.starplex.models.Booking;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowTimeDao implements BaseDao<Showtime>{

    @Override
    public void save(Showtime entity) {

    }

    @Override
    public boolean insert(Showtime entity) {
        return false;
    }

    @Override
    public void update(Showtime entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Showtime findById(long id) {
        String query = "SELECT * FROM showtimes WHERE id = ?";
        try(PreparedStatement statement = conn.prepareStatement(query)) {
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

    private Showtime mapResultSetToShowtime(ResultSet resultSet) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setId(resultSet.getInt("id"));
        showtime.setMovie(new MovieDao().findById(resultSet.getLong("movie_id")));
        showtime.setRoom(new RoomDao().findById(resultSet.getLong("room_id")));
        showtime.setShowTime(resultSet.getTime("showTime"));
        showtime.setShowDate(resultSet.getDate("showDate"));
        showtime.setPrice(resultSet.getBigDecimal("price"));
        showtime.setCreatedAt(resultSet.getDate("created_at"));
        return showtime;
    }

    @Override
    public List<Showtime> findAll() {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT * FROM showtimes";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                showtimes.add(mapResultSetToShowtime(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return showtimes;
    }

    private Connection conn;

    public ShowTimeDao() {
        this.conn = DatabaseConnection.getConn();
    }

    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.id, m.title AS movie_title, r.room_number, s.show_date, s.show_time, s.price, s.created_at " +
                "FROM showtimes s " +
                "LEFT JOIN movies m ON s.movie_id = m.id " +
                "LEFT JOIN rooms r ON s.room_id = r.id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String movieTitle = rs.getString("movie_title");
                String roomNumber = rs.getString("room_number");

                showtimes.add(new Showtime(
                        rs.getInt("id"),
                        new Movie(movieTitle),
                        new Room(roomNumber),
                        rs.getDate("show_date"),   // Lấy ngày chiếu
                        rs.getTime("show_time"),   // Lấy giờ chiếu
                        rs.getBigDecimal("price"),
                        rs.getDate("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Chèn một lịch chiếu mới
     */
    public boolean insertShowtime(Showtime showtime) {
        String sql = "INSERT INTO showtimes (movie_id, room_id, show_date, show_time, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (showtime.getMovie() != null) {
                stmt.setInt(1, showtime.getMovie().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            if (showtime.getRoom() != null) {
                stmt.setInt(2, showtime.getRoom().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setDate(3, showtime.getShowDate());   // Lưu ngày chiếu
            stmt.setTime(4, showtime.getShowTime());   // Lưu giờ chiếu
            stmt.setBigDecimal(5, showtime.getPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật lịch chiếu
     */
    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE showtimes SET movie_id = ?, room_id = ?, show_date = ?, show_time = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (showtime.getMovie() != null) {
                stmt.setInt(1, showtime.getMovie().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            if (showtime.getRoom() != null) {
                stmt.setInt(2, showtime.getRoom().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setDate(3, showtime.getShowDate());   // Cập nhật ngày chiếu
            stmt.setTime(4, showtime.getShowTime());   // Cập nhật giờ chiếu
            stmt.setBigDecimal(5, showtime.getPrice());
            stmt.setInt(6, showtime.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa lịch chiếu
     */
    public boolean deleteShowtime(int id) {
        String sql = "DELETE FROM showtimes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Showtime> getShowtimesByMovieAndDate(int id, LocalDate selectedDate) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes WHERE movie_id =? AND DATE(start_time) =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setDate(2, Date.valueOf(selectedDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                showtimes.add(new Showtime(
                        rs.getInt("id"),
                        null,
                        null,
                        rs.getDate("showDate"),
                        rs.getTime("showTime"),
                        rs.getBigDecimal("price"),
                        rs.getDate("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
}
