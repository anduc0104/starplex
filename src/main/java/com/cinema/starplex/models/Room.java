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

    public Room(int roomId, int roomNumber) {
        this.id = roomId;
        this.roomNumber = roomNumber;
    }

    public Room(int roomId) {
        this.id = roomId;
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
}
