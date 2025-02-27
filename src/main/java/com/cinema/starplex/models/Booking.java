package com.cinema.starplex.models;


import java.sql.Timestamp;
import java.math.BigDecimal;

public class Booking {
    private Integer id;
    private User user;
    private Showtime showtime;
    private BigDecimal totalPrice;
    private String status;
    private Timestamp createdAt;

    public Booking() {

    }

    public Booking(Integer id, User user, Showtime showtime, BigDecimal totalPrice, String status, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.showtime = showtime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

// Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
