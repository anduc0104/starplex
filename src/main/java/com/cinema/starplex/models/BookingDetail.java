package com.cinema.starplex.models;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class BookingDetail {
    private Integer id;
    private Booking booking;
    private Seat seat;
    private BigDecimal price;
    private Timestamp createdAt;


    public BookingDetail() {}

    public BookingDetail(Integer id, Booking booking, Seat seat, BigDecimal price, Timestamp createdAt) {
        this.id = id;
        this.booking = booking;
        this.seat = seat;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
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
