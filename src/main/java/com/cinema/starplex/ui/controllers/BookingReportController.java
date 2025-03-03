package com.cinema.starplex.ui.controllers;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.cinema.starplex.models.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class BookingReportController implements Initializable {

    @FXML
    private Label totalBookingsLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label avgBookingPriceLabel;

    @FXML
    private PieChart statusPieChart;

    @FXML
    private BarChart<String, Number> revenueByStatusChart;

    private ObservableList<Booking> bookings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code will run when FXML is loaded
    }

    public void setBookings(ObservableList<Booking> bookings) {
        this.bookings = bookings;
        updateReportData();
    }

    private void updateReportData() {
        // Calculate statistics
        int totalBookings = bookings.size();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        // Count bookings by status
        Map<String, Integer> statusCounts = new HashMap<>();
        Map<String, BigDecimal> revenueByStatus = new HashMap<>();

        for (Booking booking : bookings) {
            String status = booking.getStatus();
            BigDecimal price = booking.getTotalPrice();

            // Add to total revenue
            totalRevenue = totalRevenue.add(price);

            // Count status
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);

            // Add to revenue by status
            BigDecimal statusRevenue = revenueByStatus.getOrDefault(status, BigDecimal.ZERO);
            revenueByStatus.put(status, statusRevenue.add(price));
        }

        // Calculate average booking price
        BigDecimal avgPrice = totalBookings > 0
                ? totalRevenue.divide(new BigDecimal(totalBookings), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        // Update the labels
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        totalRevenueLabel.setText("$" + totalRevenue.toString());
        avgBookingPriceLabel.setText("$" + avgPrice.toString());

        // Update the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
        statusPieChart.setData(pieChartData);

        // Update the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue by Status");

        for (Map.Entry<String, BigDecimal> entry : revenueByStatus.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        revenueByStatusChart.getData().clear();
        revenueByStatusChart.getData().add(series);
    }
}
