package com.cinema.starplex.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

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
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(SceneSwitcher.class.getResource("/css/main.css").toExternalForm());
            scene.getStylesheets().add(SceneSwitcher.class.getResource("/css/admin.css").toExternalForm());
            if (fxmlFile == "LoginView.fxml") {
                stage.setFullScreen(true);
            }
            stage.setScene(scene);
            System.out.println("Stylesheets: " + scene.getStylesheets());

            stage.setFullScreenExitHint("");
            if (fxmlFile == "LoginView.fxml") {
                stage.setFullScreen(false);
            } else {
//                stage.setFullScreen(true);
                stage.setFullScreen(false);
            }
            stage.show();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error switching scene: " + e.getMessage());
        }
    }

    public static FXMLLoader loadView(String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/com/cinema/starplex/" + fxmlFile));
        try {
            loader.load(); // Load FXML trước
            return loader; // Trả về loader để lấy Controller
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
