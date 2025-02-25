package com.cinema.starplex.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SeatType {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Timestamp createdAt;

    public SeatType(Integer id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

// Getters and Setters
    public SeatType() {}

    public SeatType(Integer id, String name, BigDecimal price, Timestamp createdAt) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
