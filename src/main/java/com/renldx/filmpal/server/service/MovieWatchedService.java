package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.helper.AuthHelper;
import com.renldx.filmpal.server.helper.MovieHelper;
import com.renldx.filmpal.server.model.Movie;
import com.renldx.filmpal.server.model.UserMovie;
import com.renldx.filmpal.server.repository.MovieRepository;
import com.renldx.filmpal.server.repository.UserMovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashSet;
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

    public Set<Movie> getMovies() {
        return new HashSet<>(movieRepository.findAll());
    }

    public Set<Movie> getUserMovies() {
        var userId = authHelper.getUserId();
        return userMovieRepository.findAllByUserId(userId).stream().map(UserMovie::getMovie).collect(Collectors.toSet());
    }

    public Movie getMovie(int id) {
        return movieRepository.findById(id).orElseThrow();
    }

    public Optional<Movie> findMovie(String code) {
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        return movieRepository.findByTitleAndRelease(params[0], release);
    }

    public Movie getUserMovie(String code) {
        var userId = authHelper.getUserId();
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var userMovie = userMovieRepository.findByTitleAndRelease(userId, params[0], release).orElseThrow();

        return userMovie.getMovie();
    }

    public Movie createMovie(String title, Year release) {
        var movie = new Movie(title, release);

        movieRepository.save(movie);

        return movie;
    }

    @Transactional
    public Movie createUserMovie(String title, Year release) {
        var userId = authHelper.getUserId();
        var movie = findMovie(MovieHelper.getMovieCode(title, release));

        if (movie.isEmpty()) {
            movie = Optional.of(createMovie(title, release));
        }

        var userMovie = new UserMovie(userId, movie.get());
        userMovieRepository.save(userMovie);

        return movie.get();
    }

    public Movie updateMovie(int id, String title, Year release) {
        var movie = movieRepository.findById(id).orElseThrow();

        movie.setTitle(title);
        movie.setRelease(release);

        movieRepository.save(movie);

        return movie;
    }

    public Movie updateMovie(String code, String newTitle, Year newRelease) {
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var movie = movieRepository.findByTitleAndRelease(params[0], release).orElseThrow();

        movie.setTitle(newTitle);
        movie.setRelease(newRelease);

        movieRepository.save(movie);

        return movie;
    }

    @Transactional
    public Movie updateUserMovie(String code, String newTitle, Year newRelease) {
        var userId = authHelper.getUserId();
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var userMovie = userMovieRepository.findByTitleAndRelease(userId, params[0], release).orElseThrow();
        var otherUserMovies = userMovie.getMovie().getUserMovies().stream().filter(um -> !um.equals(userMovie));

        assert !userMovie.getMovie().getTitle().equals(newTitle) && !userMovie.getMovie().getRelease().equals(newRelease);

        // No other related records exist, can be updated
        if (otherUserMovies.findAny().isEmpty()) {
            return updateMovie(userMovie.getMovie().getId(), newTitle, newRelease);
        }
        // Other related records exist, must create new
        else {
            var movie = createMovie(newTitle, newRelease);
            userMovie.setMovie(movie);

            userMovieRepository.save(userMovie);

            return movie;
        }
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public void deleteMovie(String code) {
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var movie = movieRepository.findByTitleAndRelease(params[0], release);

        movie.ifPresent(m -> deleteMovie(m.getId()));
    }

    @Transactional
    public void deleteUserMovie(String code) {
        var userId = authHelper.getUserId();
        var params = MovieHelper.getMovieTitleAndRelease(code, true);
        var release = MovieHelper.getMovieRelease(params[1]);

        var userMovie = userMovieRepository.findByTitleAndRelease(userId, params[0], release).orElseThrow();
        var otherUserMovies = userMovie.getMovie().getUserMovies().stream().filter(um -> !um.equals(userMovie));

        userMovieRepository.delete(userMovie);

        // Orphan movie record, can be deleted
        if (otherUserMovies.findAny().isEmpty()) {
            deleteMovie(userMovie.getMovie().getId());
        }
    }

}
