package com.cinema.starplex.models;

public class MovieMovieGenre {
    private Integer id;
    private Movie movie;
    private MovieGenre movieGenre;

    public MovieMovieGenre() {}

    public MovieMovieGenre(Integer id, Movie movie, MovieGenre movieGenre) {
        this.id = id;
        this.movie = movie;
        this.movieGenre = movieGenre;
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

    public MovieGenre getMovieType() {
        return movieGenre;
    }

    public void setMovieType(MovieGenre movieGenre) {
        this.movieGenre = movieGenre;
    }
}