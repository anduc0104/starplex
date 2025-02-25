package com.cinema.starplex.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneSwitcher {

    public static void switchTo(Stage stage, String fxmlFile) {
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null.");
        }

        try {
            URL fxmlUrl = SceneSwitcher.class.getResource("/com/cinema/starplex/" + fxmlFile);
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found: " + fxmlFile);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(fxmlLoader.load());

            stage.setScene(scene);
            stage.setFullScreenExitHint(""); // Ẩn thông báo thoát fullscreen
            stage.setFullScreen(true);
            stage.show();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error switching scene: " + e.getMessage());
        }
    }
}
