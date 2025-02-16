package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.helper.MovieHelper;
import com.renldx.filmpal.server.model.Movie;
import com.renldx.filmpal.server.model.MovieDto;
import com.renldx.filmpal.server.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    public Optional<MovieDto> getMovie(String code) {
        var params = MovieHelper.getMovieTitleAndRelease(code);

        var release = MovieHelper.getMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        if (movie.isPresent()) {
            var movieDto = new MovieDto();
            BeanUtils.copyProperties(movie.get(), movieDto);

            return Optional.of(movieDto);
        } else {
            return Optional.empty();
        }
    }

    public MovieDto createMovie(MovieDto movieDto) {
        var movie = new Movie();
        BeanUtils.copyProperties(movieDto, movie);

        movieRepository.save(movie);

        return movieDto;
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

    public Optional<MovieDto> updateMovie(String code, MovieDto movie) {
        var params = MovieHelper.getMovieTitleAndRelease(code);

        var release = MovieHelper.getMovieRelease(params[1]);
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

    public void deleteMovie(String code) {
        var params = MovieHelper.getMovieTitleAndRelease(code);

        var release = MovieHelper.getMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        movie.ifPresent(value -> deleteMovie(value.getId()));
    }

}
