package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainLayoutController {

    public BorderPane mainBorderPane;

    public void logout(ActionEvent event) {
        SceneSwitcher.switchTo((Stage) ((Node) event.getSource()).getScene().getWindow(), "LoginView.fxml");
    }
}
