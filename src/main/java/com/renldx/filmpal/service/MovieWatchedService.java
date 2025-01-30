package com.renldx.filmpal.service;

import com.renldx.filmpal.entity.Movie;
import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.helpers.MovieHelper;
import com.renldx.filmpal.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieWatchedService {
    private final MovieRepository movieRepository;

    public MovieWatchedService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> getMovies() {
        return movieRepository.findAll().stream().map(m -> new MovieDto(m.getTitle(), m.getRelease())).collect(Collectors.toList());
    }

    public Optional<MovieDto> getMovie(int id) {
        return movieRepository.findById(id).map(m -> new MovieDto(m.getTitle(), m.getRelease()));
    }

    public Optional<MovieDto> getMovie(String code) throws Exception {
        var params = MovieHelper.GetMovieTitleAndRelease(code);
        var release = MovieHelper.GetMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        return Optional.of(new MovieDto(movie));
    }

    public MovieDto saveMovie(int id, MovieDto movie) {
        var existingMovie = movieRepository.findById(id).orElse(null);

        assert existingMovie != null;
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setRelease(movie.getRelease());

        movieRepository.save(existingMovie);

        return movie;
    }

    public MovieDto saveMovie(MovieDto movie) {
        var newMovie = new Movie(movie.getTitle(), movie.getRelease());

        movieRepository.save(newMovie);

        return movie;
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public void deleteMovie(String code) throws Exception {
        var params = MovieHelper.GetMovieTitleAndRelease(code);
        var release = MovieHelper.GetMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        movieRepository.delete(movie);
    }
}
