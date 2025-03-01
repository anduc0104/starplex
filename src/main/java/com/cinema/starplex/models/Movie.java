package com.cinema.starplex.models;

import java.sql.Date;
import java.sql.Timestamp;
import javafx.beans.property.*;


public class Movie {
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty director;
    private StringProperty actors;
    private StringProperty genre;
    private StringProperty duration;
    private StringProperty releaseDate;
    private StringProperty description;
    private StringProperty image;

    public Movie (){}

    public Movie(IntegerProperty id, StringProperty title, StringProperty director, StringProperty actors, StringProperty genre, StringProperty duration, StringProperty releaseDate, StringProperty description, StringProperty image) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
        this.image = image;
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

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
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
}