package com.cinema.starplex.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Movie {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty director = new SimpleStringProperty();
    private StringProperty actors = new SimpleStringProperty();
    private ListProperty<Genre> genres = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty duration = new SimpleStringProperty();
    private ObjectProperty<LocalDate> releaseDate = new SimpleObjectProperty<>();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty image = new SimpleStringProperty();

    public Movie() {}

    public Movie(IntegerProperty id, StringProperty title, StringProperty director,
                 StringProperty actors, ListProperty<Genre> genres, StringProperty duration,
                 ObjectProperty<LocalDate> releaseDate,
                 StringProperty description, StringProperty image) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
        this.image = image;
    }


    public Movie(StringProperty title, StringProperty duration, ObjectProperty<LocalDate> releaseDate, StringProperty description, StringProperty image) {
        this.title = title;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
        this.image = image;
    }

    public Movie(String movieTitle) {
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

    public String getDirector() {
        return director.get();
    }

    public StringProperty directorProperty() {
        return director;
    }

    public void setDirector(String director) {
        this.director.set(director);
    }

    public String getActors() {
        return actors.get();
    }

    public StringProperty actorsProperty() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors.set(actors);
    }

    public ObservableList<Genre> getGenres() {
        return genres.get();
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

    public ObjectProperty<LocalDate> releaseDateProperty() {
        return releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate.get();
    }

    public void setReleaseDate(LocalDate releaseDate) {
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

}