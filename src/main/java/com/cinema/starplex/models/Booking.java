package com.cinema.starplex.models;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Booking {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private final ObjectProperty<Showtime> showtime = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> totalPrice = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();
    private final ObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();

    public Booking(Integer id, User user, Showtime showtime, BigDecimal totalPrice, String status, Timestamp createdAt) {
        this.id.set(id);
        this.user.set(user);
        this.showtime.set(showtime);
        this.totalPrice.set(totalPrice);
        this.status.set(status);
        this.createdAt.set(createdAt);
    }

    public Booking() {

    }

    // Getter & Setter cho id
    public Integer getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Getter & Setter cho user
    public User getUser() { return user.get(); }
    public void setUser(User user) { this.user.set(user); }
    public ObjectProperty<User> userProperty() { return user; }

    // Getter & Setter cho showtime
    public Showtime getShowtime() { return showtime.get(); }
    public void setShowtime(Showtime showtime) { this.showtime.set(showtime); }
    public ObjectProperty<Showtime> showtimeProperty() { return showtime; }

    // Getter & Setter cho totalPrice
    public BigDecimal getTotalPrice() { return totalPrice.get(); }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice.set(totalPrice); }
    public ObjectProperty<BigDecimal> totalPriceProperty() { return totalPrice; }

    // Getter & Setter cho status
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }

    // Getter & Setter cho createdAt
    public Timestamp getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt.set(createdAt); }
    public ObjectProperty<Timestamp> createdAtProperty() { return createdAt; }
}
