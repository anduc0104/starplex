package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DashboardController {

    public void filterMovies(ActionEvent actionEvent) {

    }

    public void searchUsers(ActionEvent actionEvent) {

    }

    public void viewDashboard(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "/com/cinema/starplex/Dashboard-View.fxml");
    }

    public void viewTicketSales(ActionEvent actionEvent) {
    }

    public void viewMovieDetails(ActionEvent actionEvent) {

    }

    public void viewUserDetails(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), "admin/usermanagement/list-user.fxml");
    }

    public void viewRevenueReport(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),  "admin/moviemanagement/movie-view.fxml");
    }

    public void viewStaffList(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),  "StaffView.fxml");
    }

    public void logout(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),  "LoginView");
    }

    public void viewBookings(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),  "admin/booking-view/Booking-View.fxml");
    }

    public void viewRooms(ActionEvent actionEvent) {
        SceneSwitcher.switchTo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),  "admin/roommanagement/RoomView.fxml");
    }
}
