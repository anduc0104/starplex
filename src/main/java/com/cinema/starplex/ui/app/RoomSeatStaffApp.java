package com.cinema.starplex.ui.app;

import com.cinema.starplex.ui.controllers.staff.staffDetail.StaffDetailController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomSeatStaffApp extends Application {
    private GridPane seatsGrid;

    public RoomSeatStaffApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Room and Seat Management");

        // Root layout
        // Load FXML và Controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/staff/staff-detail.fxml"));
        VBox root = loader.load(); // Load giao diện từ FXML
        StaffDetailController staffDetailController = loader.getController(); // Lấy controller đã liên kết FXML


        // Top section: Room selection
        // Center section: Seats grid
        seatsGrid = new GridPane();
        seatsGrid.setAlignment(Pos.CENTER);
        seatsGrid.setHgap(5);
        seatsGrid.setVgap(5);
        seatsGrid.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(seatsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Bottom section: Legend and totals
//        root.setBottom(bottomSection);

        staffDetailController.loadSeatTypes();
        staffDetailController.loadRooms();
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/css/staff-detail.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

