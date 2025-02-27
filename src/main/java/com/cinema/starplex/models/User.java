package com.cinema.starplex.models;

import java.sql.Timestamp;


public class User {
    private Integer id;
    private String username;
    private String full_name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Timestamp createdAt;

    public User() {
    }

    public User(Integer id, String username, String full_name, String email, String password, String phone, String role, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String username, String hashedPassword) {
        this.username = username;
        this.password = hashedPassword;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}