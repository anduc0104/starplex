package com.cinema.starplex.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

public class Movie {
    private IntegerProperty id;
    private StringProperty title;
    private ListProperty<Genre> genres;
    private StringProperty duration;
    private StringProperty releaseDate;
    private StringProperty description;
    private StringProperty image;

    public Movie() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.genres = new SimpleListProperty<>(FXCollections.observableArrayList()); // Khởi tạo với danh sách rỗng
        this.duration = new SimpleStringProperty();
        this.releaseDate = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.image = new SimpleStringProperty();
    }

    public Movie(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public Movie(int id, String title, ObservableList<Genre> genres, String duration, String releaseDate, String description, String image) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.genres = new SimpleListProperty<>(genres);
        this.duration = new SimpleStringProperty(duration);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.description = new SimpleStringProperty(description);
        this.image = new SimpleStringProperty(image);
    }

    public Movie(String movieTitle) {
        this.title = new SimpleStringProperty(movieTitle);
    }

    public Movie(int movieId, String movieTitle) {
        this.id = new SimpleIntegerProperty(movieId);
        this.title = new SimpleStringProperty(movieTitle);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableList<Genre> getGenres() {
        return FXCollections.observableArrayList(genres.get()); // Trả về bản sao
    }

    public ListProperty<Genre> genresProperty() {
        return genres;
    }

    public void setGenres(ObservableList<Genre> genres) {
        this.genres.set(genres);
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public String getReleaseDate() {
        return releaseDate.get();
    }

    public StringProperty releaseDateProperty() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getImage() {
        return image.get();
    }

    public StringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    @Override
    public String toString() {
        return title.get(); // Hoặc có thể thêm thông tin khác
    }
}