package com.cinema.starplex.ui;

import com.cinema.starplex.ui.controllers.HomepageController;
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
//         montserrat = Font.loadFont(getClass().getResource("/fonts/Montserrat-Italic-VariableFont_wght.ttf").toExternalForm(), 16);

        primaryStage.setTitle("Home Page");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
