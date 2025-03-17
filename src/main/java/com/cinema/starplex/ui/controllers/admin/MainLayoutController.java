package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainLayoutController {
    @FXML
    Button buttonLogout;

    @FXML
    private BorderPane mainBorderPane;


    public void logout(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "LoginView.fxml");
    }

    public void managementUser(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/list-user.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void movieScene(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/moviemanagement/movie-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementRoom() {
        FXMLLoader loader = SceneSwitcher.loadView("admin/roommanagement/list-room.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementPeopleFilms(ActionEvent event) {
    }

    public void managementShowtime(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/showtimemanagement/showtime-list-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementSeat() {
        FXMLLoader loader = SceneSwitcher.loadView("admin/seatmanagement/seat-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementSeatType() {
        FXMLLoader loader = SceneSwitcher.loadView("admin/seattypemanagement/seat-type-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void genreScene(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/moviegenremanagement/genre-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementBooking(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/booking-view/Booking-View.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void managementPayment(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/paymentmanagement/payment-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void mangementDashboard(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/dashboard-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }
}
