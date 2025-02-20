package com.cinema.starplex.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class FooterController {

    @FXML
    private Text menuPolicy, menuShowtimes, menuNews, menuTickets, menuQuestions, menuContact;

    @FXML
    private Text footerText;


    public void initialize() {
        addHoverEffect(menuPolicy);
        addHoverEffect(menuShowtimes);
        addHoverEffect(menuNews);
        addHoverEffect(menuTickets);
        addHoverEffect(menuQuestions);
        addHoverEffect(menuContact);


        footerText.setText("© 2025 Movie Booking System");
    }

    private void addHoverEffect(Text menuItem) {
        menuItem.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        menuItem.setOnMouseEntered(e -> menuItem.setStyle("-fx-fill: #EF4444; -fx-font-size: 14px; -fx-font-weight: bold;"));
        menuItem.setOnMouseExited(e -> menuItem.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"));
    }
}