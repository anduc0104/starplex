package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainLayoutController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private void movieScene(ActionEvent event) {
        loadPage("/com/cinema/starplex/staff/list-movie-layout.fxml");
    }

    @FXML
    private void managementRoom(ActionEvent event) {
        loadPage("/com/cinema/starplex/staff/management-room.fxml");
    }

    @FXML
    private void managementSeat(ActionEvent event) {
        loadPage("/com/cinema/starplex/staff/management-seat.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            mainBorderPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("Unable to load FXML file: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "LoginView.fxml");
    }
}