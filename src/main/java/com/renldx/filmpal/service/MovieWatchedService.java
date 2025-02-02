package com.renldx.filmpal.service;

import com.renldx.filmpal.entity.Movie;
import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.helpers.MovieHelper;
import com.renldx.filmpal.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieWatchedService {
    private final MovieRepository movieRepository;

    public MovieWatchedService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Collection<MovieDto> getMovies() {
        return movieRepository.findAll().stream().map(m -> new MovieDto(m.getTitle(), m.getRelease())).collect(Collectors.toList());
    }

    public Optional<MovieDto> getMovie(int id) {
        return movieRepository.findById(id).map(m -> new MovieDto(m.getTitle(), m.getRelease()));
    }

    public Optional<MovieDto> getMovie(String code) throws Exception {
        var params = MovieHelper.GetMovieTitleAndRelease(code);

        var release = MovieHelper.GetMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        return movie.map(MovieDto::new);
    }

    public MovieDto createMovie(MovieDto movie) {
        var newMovie = new Movie(movie.getTitle(), movie.getRelease());

        movieRepository.save(newMovie);

        return movie;
    }

    public Optional<MovieDto> updateMovie(int id, MovieDto movie) {
        var existingMovie = movieRepository.findById(id);

        if (existingMovie.isPresent()) {
            existingMovie.get().setTitle(movie.getTitle());
            existingMovie.get().setRelease(movie.getRelease());

            movieRepository.save(existingMovie.get());

            return Optional.of(movie);
        }

        return Optional.empty();
    }

    public Optional<MovieDto> updateMovie(String code, MovieDto movie) throws ParseException {
        var params = MovieHelper.GetMovieTitleAndRelease(code);

        var release = MovieHelper.GetMovieRelease(params[1]);
        var existingMovie = movieRepository.findByTitleAndRelease(params[0], release);

        if (existingMovie.isPresent()) {
            existingMovie.get().setTitle(movie.getTitle());
            existingMovie.get().setRelease(movie.getRelease());

            movieRepository.save(existingMovie.get());

            return Optional.of(movie);
        }

        return Optional.empty();
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public void deleteMovie(String code) throws ParseException {
        var params = MovieHelper.GetMovieTitleAndRelease(code);

        var release = MovieHelper.GetMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        movie.ifPresent(value -> deleteMovie(value.getId()));
    }
}
