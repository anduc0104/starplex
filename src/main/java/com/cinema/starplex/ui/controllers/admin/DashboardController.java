package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.dao.BookingDao;
import com.cinema.starplex.dao.ShowTimeDao;
import com.cinema.starplex.dao.UserDao;
import com.cinema.starplex.models.Booking;
import com.cinema.starplex.ui.controllers.admin.bookingManagement.BookingReportController;
import com.cinema.starplex.util.AlertUtils;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class DashboardController {
    private ObservableList<Booking> bookingList;
    @FXML
    private Button reportButton;

    @FXML
    private Label totalBookingsLabel;

    @FXML
    private Label totalRevenueLabel;
//
//    @FXML
//    private Label avgBookingPriceLabel;
//
//    @FXML
//    private PieChart statusPieChart;
//
//    @FXML
//    private BarChart<String, Number> revenueByStatusChart;

    private BookingDao bookingDao;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookingList = FXCollections.observableArrayList();
        loadBookingData();
        updateStatistics();
       AlertUtils.showInfo("Success","Label initialized: " + (totalBookingsLabel != null));

    }

    private void loadBookingData() {
        List<Booking> bookings = bookingDao.findAll();
        bookingList = FXCollections.observableArrayList(bookings);
        AlertUtils.showInfo("Success","Total bookings: " + bookingList.size());
    }

    private void updateStatistics() {
        int totalBookings = bookingList.size();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        if (totalBookingsLabel == null || totalRevenueLabel == null) {
            System.out.println("Labels are not initialized!");
            return;
        }
        for (Booking booking : bookingList) {
            totalRevenue = totalRevenue.add(booking.getTotalPrice());
        }
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        totalRevenueLabel.setText("$" + (totalRevenue != null ? String.format("%.2f", totalRevenue) : "0.00"));
    }

    @FXML
    private void handleReportAction(ActionEvent actionEvent) {
        try {
            //load view cua report
            System.out.println(getClass().getResource("/com/cinema/starplex/admin/booking-view/Booking-Report-View.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinema/starplex/admin/booking-view/Booking-Report-View.fxml"));
            AnchorPane reportPane = loader.load();

            //thiet lap data cho report
            BookingReportController reportController = loader.getController();
            reportController.setBookings(bookingList);

            //tao mot stage co report
            Stage reportStage = new Stage();
            reportStage.setTitle("Booking Report");
            reportStage.initModality(Modality.WINDOW_MODAL);
            reportStage.initOwner(reportButton.getScene().getWindow());

            Scene scene = new Scene(reportPane);
            reportStage.setScene(scene);
            reportStage.show();;
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
