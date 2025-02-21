package com.cinema.starplex.ui;

import com.cinema.starplex.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //css
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        //font
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/Montserrat-ExtraLight.ttf"), 16);
        System.out.println(font);

        DatabaseInitializer.initializeDatabase();
        primaryStage.setTitle("Home Page");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
