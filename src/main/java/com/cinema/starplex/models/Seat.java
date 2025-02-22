package com.cinema.starplex.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    @Column(nullable = false)
    private String seatNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    public Seat(Integer id, Room room, SeatType seatType, String seatNumber, Timestamp createdAt) {
        this.id = id;
        this.room = room;
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.createdAt = createdAt;
    }

    public Seat() {}

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
}