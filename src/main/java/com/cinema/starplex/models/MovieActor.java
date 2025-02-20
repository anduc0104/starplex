package com.cinema.starplex.models;
import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name = "movie_actors")
public class MovieActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    public MovieActor() {}

    public MovieActor(Integer id, Movie movie, Actor actor) {
        this.id = id;
        this.movie = movie;
        this.actor = actor;
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

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}

