package com.cinema.starplex.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer roomNumber;
    private Integer totalSeats;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    // Constructors

    public Room(Integer id, Integer roomNumber, Integer totalSeats, Timestamp createdAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.totalSeats = totalSeats;
        this.createdAt = createdAt;
    }

    public Room() {
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