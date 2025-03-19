package com.cinema.starplex.ui.controllers.staff;

import com.cinema.starplex.dao.*;
import com.cinema.starplex.models.Movie;
import com.cinema.starplex.models.Seat;
import com.cinema.starplex.models.SeatType;
import com.cinema.starplex.models.Showtime;
import com.cinema.starplex.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.controlsfx.control.action.Action;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MovieDetail {
    public Text showtime;
    public Text roomNumber;
    public GridPane seatGrid;
    public HBox listBtnSeatType;
    @FXML
    public Text totalPrice;
    @FXML
    public Text listSelectedSeat;
    public Button btnPayment;
    private Showtime selectedShowtime;
    private Movie selectedMovie;

    private ShowTimeDao showTimeDao;
    private RoomDao roomDao;
    private SeatTypeDao seatTypeDao;
    @FXML
    private List<Seat> selectedSeats = new ArrayList<>();

    private static final int ROWS = 9;
    private static final int COLS = 14;
    public static double totalPriceToPayment = 0;

    @FXML
    public void initialize() {
        this.showTimeDao = new ShowTimeDao();
        this.roomDao = new RoomDao();
        this.seatTypeDao = new SeatTypeDao();
        loadSeatTypes();
    }

    public void loadSeatTypes() {
        List<SeatType> seatTypes = roomDao.getSeatTypes();
        for (SeatType seatType : seatTypes) {
//            System.out.println(seatType);
            HBox seatBox = new HBox(8); // Tạo nhóm gồm Button + Label
            seatBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Button seatButton = new Button();
            seatButton.setFocusTraversable(false);
            seatButton.setOnAction(Event::consume);
            seatButton.setPrefWidth(40);
            seatButton.setPrefHeight(40);
            seatButton.getStyleClass().add("btn-" + seatType.getName());

            Label seatLabel = new Label(seatType.getName());
            seatLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            seatLabel.setAlignment(javafx.geometry.Pos.CENTER);

            seatBox.getChildren().addAll(seatButton, seatLabel);
            listBtnSeatType.getChildren().add(seatBox);
        }
    }

    public void setShowtime(Showtime showtime, Movie movie) throws SQLException {

        this.selectedShowtime = showtime;
        this.selectedMovie = movie;
        //text showtime
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Time show_time = showtime.getShowTime();
        String formattedTime = timeFormat.format(new Date(show_time.getTime()));
        this.showtime.setText(formattedTime);
        //text room number
        this.roomNumber.setText(String.valueOf(showTimeDao.getRoomNumber(showtime.getRoomId())));

        loadSeatsForRoom(showtime.getRoomId());
    }

    private void loadSeatsForRoom(int roomId) throws SQLException {
        List<Seat> seats = roomDao.getSeatsForRoom(roomId);

        // Xóa grid và danh sách chọn
//        seatGrid.getChildren().clear();
//        selectedSeats.clear();

        for (Seat seat : seats) {
            Button seatButton = new Button(seat.getRow() + String.valueOf(seat.getCol_number()));
            seatButton.setPrefSize(55, 55);


            // Xác định màu ghế
            if (seat.isBooked()) {
                seatButton.setText("X");
                seatButton.setFocusTraversable(false);
                seatButton.setDisable(true);
                seatButton.getStyleClass().add("seat-booked");
            } else {
                seatButton.getStyleClass().add("btn-" + seatTypeDao.getNameById(seat.getSeat_type_id())); // Ghế trống
            }
            String rowString = seat.getRow(); // Lấy giá trị hàng từ seat
            if (rowString == null || rowString.trim().isEmpty()) {
                System.err.println("Lỗi: row null hoặc rỗng!");
                continue; // Bỏ qua ghế bị lỗi
            }

            char rowChar = rowString.trim().charAt(0); // Lấy ký tự đầu tiên sau khi loại bỏ khoảng trắng
            int rowIndex = rowChar - 'A'; // Chuyển từ 'A', 'B', 'C' -> 0, 1, 2,...

            if (rowIndex < 0 || rowIndex > 13) {
                System.err.println("Lỗi: rowIndex ngoài phạm vi! row=" + rowChar);
                continue;
            }
            int colIndex = seat.getCol_number() - 1;
//            System.out.println(rowIndex + " - " + colIndex);
//            System.out.println("Row: " + seat.getRow() + " -> Row Index: " + rowIndex);

            seatButton.setOnAction(e -> toggleSeatSelection(seat, seatButton));

            seatGrid.setGridLinesVisible(true);
            seatGrid.add(seatButton, colIndex, rowIndex);
            seatGrid.requestLayout();
        }
    }

    public void toggleSeatSelection(Seat seat, Button seatButton) {
        if (selectedSeats.contains(seat)) {
            //bo chon
//            System.out.println("Selected ");
            selectedSeats.remove(seat);
            seatButton.getStyleClass().remove("seat-selected");

        } else {
//            System.out.println("Not Selected ");
            //them vao danh sach chon
            selectedSeats.add(seat);
            seatButton.getStyleClass().add("seat-selected");
        }
        updateSelectedSeats();
    }

    private void updateSelectedSeats() {
        // Hiển thị danh sách ghế đã chọn (row + col_number)
        StringBuilder seatText = new StringBuilder();
        // double total = 0;

        for (Seat seat : selectedSeats) {
            seatText.append(seat.getRow()).append(seat.getCol_number()).append(", ");

            // Lấy giá của ghế từ SeatTypeDao
            try {
                double seatPrice = seatTypeDao.getPriceById(seat.getSeat_type_id());
                totalPriceToPayment += seatPrice;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Xóa dấu phẩy cuối cùng nếu có ghế
        if (!selectedSeats.isEmpty()) {
            seatText.setLength(seatText.length() - 2);
        }

        listSelectedSeat.setText(seatText.toString());  // Cập nhật danh sách ghế
        totalPrice.setText(formatCurrency(totalPriceToPayment)); // Cập nhật tổng giá
        updatePaymentButtonState();
    }

    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,###,### đ");
        return df.format(amount);
    }

    public void handlePayment(ActionEvent event) {
        FXMLLoader loader = SceneSwitcher.loadView("staff/select-payment-method.fxml");
        if (loader != null) {
            SelectPaymentMethod controller = loader.getController();
//            System.out.println("jhfbajhsdfbsjahfbsaj: "+ selectedShowtime);
            controller.setInfo(selectedMovie, selectedShowtime, selectedSeats, totalPriceToPayment);

            Parent newView = loader.getRoot();
            AnchorPane anchorPane = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
            BorderPane mainPane = (BorderPane) anchorPane.lookup("#mainBorderPane");

            if (mainPane != null) {
                mainPane.setCenter(newView);
            } else {
                System.err.println("BorderPane with ID 'mainBorderPane' not found");
            }
        } else {
            System.err.println("Could not load add-seat.fxml");
        }
    }

    private void updatePaymentButtonState() {
        btnPayment.setDisable(selectedSeats.isEmpty());
    }

}
