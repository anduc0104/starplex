package com.cinema.starplex.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;

@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private Timestamp startTime;
    private BigDecimal price;
    private Timestamp createdAt;

    public Showtime(Integer id, Movie movie, Room room, Timestamp startTime, BigDecimal price, Timestamp createdAt) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Showtime(){}

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
