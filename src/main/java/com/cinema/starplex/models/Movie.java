package com.cinema.starplex.models;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Timestamp;

public class Movie {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final FloatProperty duration = new SimpleFloatProperty();
    private final ObjectProperty<Date> releaseDate = new SimpleObjectProperty<>();
    private final StringProperty rating = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty director = new SimpleStringProperty();
    private final StringProperty actors = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty image = new SimpleStringProperty();
    private final ObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();

    public Movie() {}

    public Movie(Integer id, String title, float duration, Date releaseDate, String rating,
                 String description, String director, String actors, String genre,
                 String image, Timestamp createdAt) {
        this.id.set(id);
        this.title.set(title);
        this.duration.set(duration);
        this.releaseDate.set(releaseDate);
        this.rating.set(rating);
        this.description.set(description);
        this.director.set(director);
        this.actors.set(actors);
        this.genre.set(genre);
        this.image.set(image);
        this.createdAt.set(createdAt);
    }

    // Getters & Setters
    public Integer getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }

    public float getDuration() { return duration.get(); }
    public void setDuration(float duration) { this.duration.set(duration); }
    public FloatProperty durationProperty() { return duration; }

    public Date getReleaseDate() { return releaseDate.get(); }
    public void setReleaseDate(Date releaseDate) { this.releaseDate.set(releaseDate); }
    public ObjectProperty<Date> releaseDateProperty() { return releaseDate; }

    public String getRating() { return rating.get(); }
    public void setRating(String rating) { this.rating.set(rating); }
    public StringProperty ratingProperty() { return rating; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public String getDirector() { return director.get(); }
    public void setDirector(String director) { this.director.set(director); }
    public StringProperty directorProperty() { return director; }

    public String getActors() { return actors.get(); }
    public void setActors(String actors) { this.actors.set(actors); }
    public StringProperty actorsProperty() { return actors; }

    public String getGenre() { return genre.get(); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public StringProperty genreProperty() { return genre; }

    public String getImage() { return image.get(); }
    public void setImage(String image) { this.image.set(image); }
    public StringProperty imageProperty() { return image; }

    public Timestamp getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt.set(createdAt); }
    public ObjectProperty<Timestamp> createdAtProperty() { return createdAt; }
}
