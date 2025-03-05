package com.cinema.starplex.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SeatType {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Timestamp createdAt;

    // Constructor
    public SeatType(Integer id, String name, BigDecimal price, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
    }

    public SeatType(Integer seatTypeId, String seatType) {
        this.id = seatTypeId;
        this.name = seatType;
    }

    public SeatType() {

    }

    public SeatType(int seatTypeId) {
        this.id = seatTypeId;
    }

    // Getter & Setter
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

    @Override
    public String toString() {
        return "SeatType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}