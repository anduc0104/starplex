package com.cinema.starplex.models;



import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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

    public Showtime(Integer id, Movie movie, Room room, Date showDate, Time showTime, BigDecimal price, Date createdAt) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.showDate = showDate;
        this.showTime = showTime;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Showtime() {}

    public Showtime(int id, Object o, Object o1, Time showTime, Date showDate, BigDecimal price, Timestamp createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.id = id;
            this.movie = (Movie) o;
            this.room = (Room) o1;
            this.showTime = showTime;
            this.showDate = showDate;
            this.price = price;
            this.createdAt = createdAt == null? null : new Date(createdAt.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String getDisplayName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String movieTitle = (movie != null) ? movie.getTitle() : "Unknown Movie";
        Integer roomName = (room != null) ? room.getRoomNumber() : 0;
        String showDateStr = (showDate != null) ? dateFormat.format(showDate) : "Unknown Date";
        String showTimeStr = (showTime != null) ? timeFormat.format(showTime) : "Unknown Time";
        System.out.println("Movie: " + movieTitle + ", Room: " + roomName + ", Start Time: " + showDateStr + "," + showTimeStr);
        return movieTitle + " - Room " + roomName + " - " + showDateStr + " - " + showTimeStr;
    }
}
