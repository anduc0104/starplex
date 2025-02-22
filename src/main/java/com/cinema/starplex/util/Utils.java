package com.cinema.starplex.util;

import com.cinema.starplex.Controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {
    public static void switchTo(Stage stage, String view, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}