package com.cinema.starplex.models;

import java.sql.Timestamp;
import java.util.List;

public class Room {
    private Integer id;
    private Integer roomNumber;
    private Integer totalSeats;
    private Timestamp createdAt;

    public Room(Integer id, Integer roomNumber, Integer totalSeats, Timestamp createdAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.totalSeats = totalSeats;
        this.createdAt = createdAt;
    }

    public Room() {
    }

    public Room(int id, int roomNumber, int totalSeats) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.totalSeats = totalSeats;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Room(int roomNumber, int totalSeats) {
        this.roomNumber = roomNumber;
        this.totalSeats = totalSeats;
    }

    public Room(String roomNumber) {
        this.roomNumber = Integer.parseInt(roomNumber);
    }


    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.valueOf(roomNumber);
    }
}
