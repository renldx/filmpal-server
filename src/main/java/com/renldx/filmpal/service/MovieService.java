package com.renldx.filmpal.service;

import com.renldx.filmpal.entity.Movie;
import com.renldx.filmpal.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(int id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie saveMovie(Date release, String title) {
        return saveMovie(new Movie(release, title));
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }
}
