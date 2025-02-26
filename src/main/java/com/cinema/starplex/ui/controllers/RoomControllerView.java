package com.cinema.starplex.ui.controllers;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.dao.ShowtimeDao;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Showtime;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RoomControllerView {
    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField totalSeatsField;
    @FXML
    private TextField showtimeIdField;
    @FXML
    private TextField showtimeStartTimeField;

    private RoomDao roomDao = new RoomDao();
    private ShowtimeDao showTimeDao = new ShowtimeDao();

    @FXML
    public void handleSaveRoom() {
        int roomNumber = -1;
        int totalSeats = -1;
        try {
            roomNumber = Integer.parseInt(roomNumberField.getText().trim());
            if (roomNumber <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Please enter format validate room number(1, 2, 3...)!");
            return;
        }

        try {
            totalSeats = Integer.parseInt(totalSeatsField.getText().trim());
            if (totalSeats <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Please enter format validate room's total seats(1, 2, 3...)!");
            return;
        }

        try {
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setTotalSeats(totalSeats);

            // Lưu vào database
            roomDao.save(room);
            System.out.println("Room added successfully!");

            BigDecimal price = new BigDecimal(showtimeIdField.getText().trim());
            Timestamp startTime = Timestamp.valueOf(showtimeStartTimeField.getText().trim());

            Showtime showtime = new Showtime();
            showtime.setRoom(room);
            showtime.setPrice(price);
            showtime.setStartTime(startTime);

            showTimeDao.save(showtime);
            System.out.println("Showtime added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter correct date and time format (YYYY-MM-DD HH:MM:SS)!");
        }
    }
}

