package com.cinema.starplex.models;

import java.sql.Timestamp;
public class MovieActor {
    private Integer id;
    private Movie movie;
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

