package com.cinema.starplex.models;

import java.sql.Timestamp;

public class Seat {
    private Integer id;
    private Room room;
    private SeatType seatType;
    private char row;
    private int colNumber;
    private Timestamp createdAt;
    private boolean isBooked;

    public Seat() {}

    public Seat(Integer id, Room room, SeatType seatType, char row, int colNumber, Timestamp createdAt, boolean isBooked) {
        setRow(row);
        setColNumber(colNumber);
        this.id = id;
        this.room = room;
        this.seatType = seatType;
        this.createdAt = createdAt;
        this.isBooked = isBooked;
    }

    public Seat(Integer id, char rowChar, int colNum, int seatTypeId, boolean isBooked) {
        setRow(rowChar);
        setColNumber(colNum);
        this.id = id;
        this.seatType = new SeatType(seatTypeId);
        this.isBooked = isBooked;
    }

    public Seat(Integer id, Room selectedRoom, SeatType selectedSeatType, char row, int colNumber, Timestamp timestamp) {
        setRow(row);
        setColNumber(colNumber);
        this.id = id;
        this.room = selectedRoom;
        this.seatType = selectedSeatType;
        this.createdAt = timestamp;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public char getRow() {
        return row; // Trả về row
    }

    // Setter cho row với kiểm tra
    public void setRow(char row) {
        if (row >= 'A' && row <= 'I') {
            this.row = row; // Cập nhật row nếu hợp lệ
        } else {
            throw new IllegalArgumentException("Row must be between A and I.");
        }
    }

    public int getColNumber() {
        return colNumber; // Trả về colNumber
    }

    // Setter cho colNumber với kiểm tra
    public void setColNumber(int colNumber) {
        if (colNumber >= 1 && colNumber <= 14) {
            this.colNumber = colNumber; // Cập nhật colNumber nếu hợp lệ
        } else {
            throw new IllegalArgumentException("Column number must be between 1 and 14.");
        }
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Seat seat = (Seat) obj;
        return id.equals(seat.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    // Phương thức để lấy seatNumber từ row và colNumber
    public String getSeatNumber() {
        return row + String.valueOf(colNumber);
    }

    // Lấy cột (column) của ghế
    public int getColumn() {
        return colNumber; // Trả về colNumber
    }
}