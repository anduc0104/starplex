package com.cinema.starplex.ui.controllers.staff.staffDetail;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class HompageStaffConroller {
    public void staffDetail(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "/com/cinema/starplex/staff/staff-detail.fxml");
    }
}

