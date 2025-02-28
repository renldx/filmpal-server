package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.helper.AuthHelper;
import com.renldx.filmpal.server.helper.MovieHelper;
import com.renldx.filmpal.server.model.Movie;
import com.renldx.filmpal.server.model.MovieDto;
import com.renldx.filmpal.server.model.UserMovie;
import com.renldx.filmpal.server.repository.MovieRepository;
import com.renldx.filmpal.server.repository.UserMovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieWatchedService {

    private final AuthHelper authHelper;
    private final MovieRepository movieRepository;
    private final UserMovieRepository userMovieRepository;

    public MovieWatchedService(AuthHelper authHelper, MovieRepository movieRepository, UserMovieRepository userMovieRepository) {
        this.authHelper = authHelper;
        this.movieRepository = movieRepository;
        this.userMovieRepository = userMovieRepository;
    }

    public Set<MovieDto> getMovies() {
        return movieRepository.findAll().stream().map(m -> new MovieDto(m.getTitle(), m.getRelease())).collect(Collectors.toSet());
    }

    public Set<Movie> getMoviesByUser() {
        var userId = authHelper.getUserId();
        return userMovieRepository.findAllByUserId(userId).stream().map(UserMovie::getMovie).collect(Collectors.toSet());
    }

    public Optional<MovieDto> getMovie(int id) {
        return movieRepository.findById(id).map(m -> new MovieDto(m.getTitle(), m.getRelease()));
    }

    public Optional<MovieDto> getMovie(String code) {
        var params = MovieHelper.getMovieTitleAndRelease(code, true);

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

    public Movie getMovieByUser(String code) {
        var userId = authHelper.getUserId();
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var userMovie = userMovieRepository.findByTitleAndRelease(userId, params[0], release);

        return userMovie.getMovie();
    }

    public MovieDto createMovie(MovieDto movieDto) {
        var movie = new Movie();
        BeanUtils.copyProperties(movieDto, movie);

        movieRepository.save(movie);

        return movieDto;
    }

    @Transactional
    public Movie createMovieByUser(String title, Year release) {
        var movie = new Movie(title, release);
        movieRepository.save(movie);

        var userMovie = new UserMovie(authHelper.getUserId(), movie);
        userMovieRepository.save(userMovie);

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

    public Optional<MovieDto> updateMovie(String code, MovieDto movie) {
        var params = MovieHelper.getMovieTitleAndRelease(code, true);

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
        var params = MovieHelper.getMovieTitleAndRelease(code, true);

        var release = MovieHelper.getMovieRelease(params[1]);
        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        movie.ifPresent(value -> deleteMovie(value.getId()));
    }

}
