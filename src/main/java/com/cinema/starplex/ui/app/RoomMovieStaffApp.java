package com.cinema.starplex.ui.app;

import com.cinema.starplex.ui.controllers.staff.staffDetail.HompageStaffConroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomMovieStaffApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the staff homepage FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/staff/staff-hompage.fxml"));
            Parent root = loader.load();

            // Get the controller
            HompageStaffConroller controller = loader.getController();

            // Set up the scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/staff-detail.css").toExternalForm());

            // Set up the stage
            primaryStage.setTitle("Starplex Cinema - Staff Movie Management");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(720);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
