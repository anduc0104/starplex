package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.util.Utils;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class DashboardController {

    public void filterMovies(ActionEvent actionEvent) {

    }

    public void searchUsers(ActionEvent actionEvent) {

    }

    public void viewMovieDetails(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/ui/views/movie_details.fxml", "Movie Details");
    }

    public void viewUserDetails(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/ui/views/movie_details.fxml", "Movie Details");
    }

    public void viewRevenueReport(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/ui/views/movie_details.fxml", "Movie Details");
    }

    public void viewStaffList(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/ui/views/movie_details.fxml", "Movie Details");
    }

    public void viewChart(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/ui/views/movie_details.fxml", "Movie Details");
    }

    public void logout(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/LoginView", "Logout");
    }

    public void viewRooms(ActionEvent actionEvent) {
        Utils.switchScene((Node) actionEvent.getSource(), "/com/cinema/starplex/RoomView.fxml", "Movie Details");
    }
}
