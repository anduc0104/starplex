package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.session.SessionManager;
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
//        FXMLLoader loader = SceneSwitcher.loadView("LoginView.fxml");
//        assert loader != null;
//        Parent newView = loader.getRoot();
//        mainBorderPane.setCenter(newView);

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        SceneSwitcher.switchTo(new Stage(), "LoginView.fxml");

        SessionManager.getInstance().clearSession();
    }

    public void managementUser(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/usermanagement/list-user.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void dashBoardScene(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/dashboard-view.fxml");
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
        FXMLLoader loader = SceneSwitcher.loadView("admin/showtimesmanagement/showtime-view.fxml");
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

    public void managementBooking(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/booking-view/Booking-View.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void movieGenreScene(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/moviegenremanagement/genre-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }

    public void showtimeScene(ActionEvent actionEvent) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/showtimesmanagement/showtime-view.fxml");
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

    public void managementPayment(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("admin/paymentmanagement/payment-view.fxml");
        assert loader != null;
        Parent newView = loader.getRoot();
        mainBorderPane.setCenter(newView);
    }
}
