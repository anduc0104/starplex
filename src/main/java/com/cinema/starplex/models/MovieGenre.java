package com.cinema.starplex.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "movie_genres")
public class MovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "movie_type_id")
    private MovieType movieType;

    public MovieGenre() {}

    public MovieGenre(Integer id, Movie movie, MovieType movieType) {
        this.id = id;
        this.movie = movie;
        this.movieType = movieType;
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

    public MovieType getMovieType() {
        return movieType;
    }

    public void setMovieType(MovieType movieType) {
        this.movieType = movieType;
    }
}
