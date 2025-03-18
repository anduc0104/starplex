package com.cinema.starplex.models;

import java.sql.Timestamp;

public class MovieGenre {
    private Integer id;
    private String name;
    private Timestamp createdAt;

    public MovieGenre(Integer id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public MovieGenre() {
    }

    public MovieGenre(int id, String name) {
        this.id = id;
        this.name = name;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}