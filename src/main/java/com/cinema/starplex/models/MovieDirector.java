package com.cinema.starplex.models;
import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@Table(name = "movie_directors")
public class MovieDirector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    public MovieDirector() {}

    public MovieDirector(Integer id, Movie movie, Director director) {
        this.id = id;
        this.movie = movie;
        this.director = director;
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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
