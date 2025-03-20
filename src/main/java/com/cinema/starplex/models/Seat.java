package com.cinema.starplex.models;


import java.sql.Timestamp;

public class Seat {
    private Integer id;
    private Room room;
    private SeatType seatType;
    private String seatNumber;
    private Timestamp createdAt;
    private boolean isBooked;
    private String row;
    private Integer col_number;
    private Integer seat_type_id;

    public Seat() {}

    public Seat(Integer id, Room room, SeatType seatType, String seatNumber, Timestamp createdAt, boolean isBooked) {
        this.id = id;
        this.room = room;
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.createdAt = createdAt;
        this.isBooked = isBooked;
    }

    public Seat(Integer id, String rowChar, Integer colNum, int seatTypeId, boolean isBooked) {
        this.id = id;
        this.row = rowChar;
        this.col_number = colNum;
        this.seat_type_id = seatTypeId;
        this.isBooked = isBooked;
    }

    public Seat(Integer id, Room selectedRoom, SeatType selectedSeatType, String seatNumber, Timestamp timestamp) {
        this.id = id;
        this.room = selectedRoom;
        this.seatType = selectedSeatType;
        this.seatNumber = seatNumber;
        this.createdAt = timestamp;
    }

    public Seat(Room selectedRoom, SeatType selectedSeatType, String row, Integer col_number) {
        this.room = selectedRoom;
        this.seatType = selectedSeatType;
        this.row = row;
        this.col_number = col_number;
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

    public void setRow(String row) {
        this.row = row;
    }

    public Integer getCol_number() {
        return col_number;
    }

    public String getRow() {
        return row;
    }

    public void setCol_number(Integer col_number) {
        this.col_number = col_number;
    }

    public Integer getSeat_type_id() {
        return seat_type_id;
    }

    public void setSeat_type_id(Integer seat_type_id) {
        this.seat_type_id = seat_type_id;
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

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", room=" + room +
                ", seatType=" + seatType +
                ", seatNumber='" + seatNumber + '\'' +
                ", createdAt=" + createdAt +
                ", isBooked=" + isBooked +
                ", row=" + row +
                ", col_number=" + col_number +
                '}';
    }
}
