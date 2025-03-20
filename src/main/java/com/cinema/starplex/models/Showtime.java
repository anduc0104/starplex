package com.cinema.starplex.models;

import java.sql.Date;
import java.sql.Time;
import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Movie;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Showtime {
    private Integer id;
    private Movie movie;
    private Room room;
    private Date showDate;
    private Time showTime;
    private BigDecimal price;
    private Date createdAt;
    private Integer movieId;
    private Integer roomId;

    public Showtime(Integer id, Movie movie, Room room, Date showDate, Time showTime, BigDecimal price, Date createdAt) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.showDate = showDate;
        this.showTime = showTime;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Showtime() {
    }

    public Showtime(Integer id, Integer movieId, Integer roomId, Time showtime) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.showTime = showtime;
    }

    public Showtime(Integer id, String movieTitle, int roomNumber, Date showDate, Time showTime) {
        this.id = id;
        this.movie = new Movie();
        this.movie.setTitle(movieTitle);

        this.room = new Room();
        this.room.setRoomNumber(roomNumber);

        this.showDate = showDate;
        this.showTime = showTime;
    }

    public Showtime(int id, Time showTime) {
        this.id = id;
        this.showTime = showTime;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public Time getShowTime() {
        return showTime;
    }

    public void setShowTime(Time showTime) {
        this.showTime = showTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getDisplayName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String movieTitle = (movie != null) ? movie.getTitle() : "Unknown Movie";
            int roomNumber = (room != null) ? room.getRoomNumber() : 0;
        String showDateStr = (showDate != null) ? dateFormat.format(showDate) : "Unknown Date";
        String showTimeStr = (showTime != null) ? timeFormat.format(showTime) : "Unknown Time";

        return movieTitle + " - Room " + roomNumber + " - " + showDateStr + " - " + showTimeStr;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", movie=" + movie +
                ", room=" + room +
                ", showDate=" + showDate +
                ", showTime=" + showTime +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", movieId=" + movieId +
                ", roomId=" + roomId +
                '}';
    }
}