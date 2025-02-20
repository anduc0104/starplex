package com.cinema.starplex.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class HeaderController {

    @FXML
    private Text menuHome, menuShowtimes, menuNews, menuPromotions, menuTickets, menuFilmWeek, menuAbout;

    @FXML
    private Button btnRegister, btnLogin;

    public void initialize() {
        // Thêm hiệu ứng hover cho menu
        addHoverEffect(menuHome);
        addHoverEffect(menuShowtimes);
        addHoverEffect(menuNews);
        addHoverEffect(menuPromotions);
        addHoverEffect(menuTickets);
        addHoverEffect(menuFilmWeek);
        addHoverEffect(menuAbout);

        // Thêm hiệu ứng phóng to cho nút
        addZoomEffect(btnRegister);
        addZoomEffect(btnLogin);
    }

    private void addHoverEffect(Text menuItem) {
        menuItem.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        menuItem.setOnMouseEntered(e -> menuItem.setStyle("-fx-fill: #EF4444; -fx-font-size: 14px; -fx-font-weight: bold;"));
        menuItem.setOnMouseExited(e -> menuItem.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"));
    }

    private void addZoomEffect(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.setOnMouseEntered(e -> scaleTransition.playFromStart());
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }
}