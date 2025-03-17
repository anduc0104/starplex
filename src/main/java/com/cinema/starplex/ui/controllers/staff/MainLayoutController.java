package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private BorderPane mainBorderPane;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void initialize() {
        // TODO: Implement initialization logic
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/staff/list-movie-layout.fxml"));
            Parent listMovie = loader.load();
            mainBorderPane.setCenter(listMovie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Xử lý sự kiện khi nhấn nút "Movie"
    @FXML
    private void movieScene(ActionEvent event) throws IOException {
        changeScene(event, "/com/cinema/starplex/staff/list-movie-layout.fxml");
    }

    // Xử lý sự kiện khi nhấn nút "Room"
    @FXML
    private void managementRoom(ActionEvent event) throws IOException {
        changeScene(event, "/com/cinema/starplex/staff/.fxml");
    }

    // Xử lý sự kiện khi nhấn nút "Seat"
    @FXML
    private void managementSeat(ActionEvent event) throws IOException {
        changeScene(event, "/com/cinema/starplex/staff/.fxml");
    }

    // Hàm chung để chuyển scene
    private void changeScene(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "LoginView.fxml");
    }
}