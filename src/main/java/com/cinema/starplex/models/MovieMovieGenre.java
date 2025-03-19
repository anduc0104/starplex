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

    public MovieGenre getMovieGenre() { // Đổi tên phương thức
        return movieGenre;
    }

    public void setMovieGenre(MovieGenre movieGenre) { // Đổi tên phương thức
        this.movieGenre = movieGenre;
    }

    @Override
    public String toString() {
        return "MovieMovieGenre{" +
                "id=" + id +
                ", movie=" + movie.getTitle() + // Hoặc bất kỳ thuộc tính nào của Movie bạn muốn hiển thị
                ", movieGenre=" + movieGenre.getName() + // Hoặc bất kỳ thuộc tính nào của MovieGenre bạn muốn hiển thị
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieMovieGenre)) return false;
        MovieMovieGenre that = (MovieMovieGenre) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}