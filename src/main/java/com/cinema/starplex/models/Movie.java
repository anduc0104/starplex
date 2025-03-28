package com.cinema.starplex.models;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class Movie {
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty director;
    private StringProperty actors;
    private ListProperty<Genre> genres;
    private StringProperty duration;
    private StringProperty releaseDate;
    private StringProperty description;
    private StringProperty image;

    public Movie() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.director = new SimpleStringProperty();
        this.actors = new SimpleStringProperty();
        this.genres = new SimpleListProperty<>();
        this.duration = new SimpleStringProperty();
        this.releaseDate = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.image = new SimpleStringProperty();
    }

    public Movie(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public Movie(int id, String title, String director, String actors, ObservableList<Genre> genres, String duration, String releaseDate, String description, String image) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.director = new SimpleStringProperty(director);
        this.actors = new SimpleStringProperty(actors);
        this.genres = new SimpleListProperty<>(genres);
        this.duration = new SimpleStringProperty(duration);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.description = new SimpleStringProperty(description);
        this.image = new SimpleStringProperty(image);
    }

    public Movie(String movieTitle) {
        this.title = new SimpleStringProperty(movieTitle);
    }

    public <E> Movie(int movieId, String title, ObservableList<Genre> es, String duration, String releaseDate, String description, String images) {
        this.id = new SimpleIntegerProperty(movieId);
        this.title = new SimpleStringProperty(title);
        this.genres = new SimpleListProperty<>(es);
        this.duration = new SimpleStringProperty(duration);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.description = new SimpleStringProperty(description);
        this.image = new SimpleStringProperty(images);
    }

    public Movie(Integer movieId, String movieTitle) {
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
        return getTitle(); // Chỉ trả về title, không in toàn bộ object
    }
}