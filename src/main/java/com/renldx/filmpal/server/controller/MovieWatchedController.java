package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.payload.request.MovieCreateRequest;
import com.renldx.filmpal.server.payload.request.MovieUpdateRequest;
import com.renldx.filmpal.server.payload.response.MovieResponse;
import com.renldx.filmpal.server.service.MovieWatchedService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/watched/")
public class MovieWatchedController {

    private final Logger log = LoggerFactory.getLogger(MovieWatchedController.class);
    private final MovieWatchedService movieWatchedService;

    public MovieWatchedController(MovieWatchedService movieWatchedService) {
        this.movieWatchedService = movieWatchedService;
    }

    @GetMapping("/movies")
    public Set<MovieResponse> getWatchedMovies() {
        var movies = movieWatchedService.getMovies();
        return movies.stream().map(m -> new MovieResponse(m.getTitle(), m.getRelease())).collect(Collectors.toSet());
    }

    @GetMapping("/moviesByUser")
    public Set<MovieResponse> getWatchedMoviesByUser() {
        var userMovies = movieWatchedService.getUserMovies();
        return userMovies.stream().map(m -> new MovieResponse(m.getTitle(), m.getRelease())).collect(Collectors.toSet());
    }

    @GetMapping("/movie/{id}")
    public MovieResponse getWatchedMovie(@PathVariable int id) {
        var movie = movieWatchedService.getMovie(id);
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @GetMapping("/movie")
    public MovieResponse getWatchedMovie(@RequestParam(value = "code") String code) {
        var movie = movieWatchedService.findMovie(code).orElseThrow();
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @GetMapping("/movieByUser")
    public MovieResponse getWatchedMovieByUser(@RequestParam(value = "code") String code) {
        var movie = movieWatchedService.getUserMovie(code);
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieResponse> createWatchedMovie(@Valid @RequestBody MovieCreateRequest request) throws URISyntaxException {
        var movie = movieWatchedService.createMovie(request.title(), request.release());
        var response = new MovieResponse(movie.getTitle(), movie.getRelease());

        return ResponseEntity.created(new URI("/api/watched/movie?code=" + response.getCode())).body(response);
    }

    @PostMapping("/movieByUser")
    public ResponseEntity<MovieResponse> createWatchedMovieByUser(@Valid @RequestBody MovieCreateRequest request) throws URISyntaxException {
        var movie = movieWatchedService.createUserMovie(request.title(), request.release());
        var response = new MovieResponse(movie.getTitle(), movie.getRelease());

        return ResponseEntity.created(new URI("/api/watched/movie?code=" + response.getCode())).body(response);
    }

    @PutMapping("/movie/{id}")
    public MovieResponse updateWatchedMovie(@Valid @RequestBody MovieUpdateRequest request, @PathVariable int id) {
        var movie = movieWatchedService.updateMovie(id, request.title(), request.release());
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @PutMapping("/movie")
    public MovieResponse updateWatchedMovie(@Valid @RequestBody MovieUpdateRequest request, @RequestParam(value = "code") String code) {
        var movie = movieWatchedService.updateMovie(code, request.title(), request.release());
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteWatchedMovie(@PathVariable int id) {
        movieWatchedService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/movie")
    public ResponseEntity<?> deleteWatchedMovie(@RequestParam(value = "code") String code) {
        movieWatchedService.deleteMovie(code);
        return ResponseEntity.ok().build();
    }

}
