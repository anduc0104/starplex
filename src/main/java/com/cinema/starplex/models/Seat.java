package com.cinema.starplex.models;


import java.sql.Timestamp;

public class Seat {
    private Integer id;
    private Room room;
    private SeatType seatType;
    private String seatNumber;
    private Timestamp createdAt;
    private boolean isBooked;

    public Seat() {}

    public Seat(Integer id, Room room, SeatType seatType, String seatNumber, Timestamp createdAt, boolean isBooked) {
        this.id = id;
        this.room = room;
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.createdAt = createdAt;
        this.isBooked = isBooked;
    }

    public Seat(Integer id, char rowChar, int colNum, int seatTypeId, boolean isBooked) {
        this.id = id;
        this.seatNumber = rowChar + String.valueOf(colNum);
        this.seatType = new SeatType(seatTypeId);
        this.isBooked = isBooked;
    }

    public Seat(Integer id, Room selectedRoom, SeatType selectedSeatType, String seatNumber, Timestamp timestamp) {
        this.id = id;
        this.room = selectedRoom;
        this.seatType = selectedSeatType;
        this.seatNumber = seatNumber;
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

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
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
        return id == seat.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public char getRow() {
        if (seatNumber != null && seatNumber.length() > 0) {
            return seatNumber.charAt(0);
        }
        return ' ';
    }

    // Lấy cột (column) của ghế (phần số sau ký tự đầu tiên)
    public int getColumn() {
        if (seatNumber != null && seatNumber.length() > 1) {
            try {
                return Integer.parseInt(seatNumber.substring(1));
            } catch (NumberFormatException e) {
                return -1; // Trả về -1 nếu xảy ra lỗi khi parse số
            }
        }
        return -1;
    }
}
