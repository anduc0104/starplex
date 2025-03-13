package com.cinema.starplex.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;


public class User {
    private Integer id;
    private String fullName;
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

//    public User(String fullName,String username, String email, String password, String phone, String role) {
//        this.fullName = fullName;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.role = role;
//    }

    public User(String username, String hashedPassword) {
        this.username = username;
        this.password = hashedPassword;
    }

    public User(int id, String fullname, String username, String email, String phone, String role) {
        this.id = id;
        this.full_name = fullname;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

//    public User(String fullName, String username, String email, String password, String phone, String role) {
//        this.full_name = fullName;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.role = role;
//    }

    public User(int id, String fullName, String username, String email, String phone, String role, String password) {
        this.id = id;
        this.full_name = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.password = password;
    }

    public User(String fullName, String username, String email, String password, String phone, String role) {
        this.full_name = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

//    public User(IntegerProperty id, StringProperty fullname, StringProperty username, StringProperty email, StringProperty phone, StringProperty role) {
//        this.id = id;
//        this.full_name = String.valueOf(fullname);
//        this.username = String.valueOf(username);
//        this.email = String.valueOf(email);
//        this.phone = phone;
//        this.role = role;
//    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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