package com.cinema.starplex.ui.controllers.staff.staffDetail;

import com.cinema.starplex.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SeatLoadPrice {
    private static Connection connection;

    public SeatLoadPrice() {
        this.connection = DatabaseConnection.getConn();
    }

    public SeatLoadPrice(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static Map<String, Integer> loadSeatPrice() {
        Map<String, Integer> seatPrice = new HashMap<>();
        try {
            String sql = "SELECT st.price, s.seat_number FROM seat_types st"
                    + " JOIN seats s ON st.id = seats.seat_type_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                seatPrice.put(resultSet.getString("seat_number"), resultSet.getInt("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seatPrice;
    }
}
