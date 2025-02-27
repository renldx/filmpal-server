package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.constant.ExceptionMessages;
import com.renldx.filmpal.server.model.MovieDto;
import com.renldx.filmpal.server.service.MovieWatchedService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/watched/")
public class MovieWatchedController {

    private final Logger log = LoggerFactory.getLogger(MovieWatchedController.class);
    private final MovieWatchedService movieWatchedService;

    public MovieWatchedController(MovieWatchedService movieWatchedService) {
        this.movieWatchedService = movieWatchedService;
    }

    @GetMapping("/movies")
    public Set<MovieDto> getWatchedMovies() {
        return movieWatchedService.getMovies();
    }

    @GetMapping("/moviesByUser")
    public Set<MovieDto> getWatchedMoviesByUser() {
        return movieWatchedService.getMoviesByUser();
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getWatchedMovie(@PathVariable int id) {
        Optional<MovieDto> movie = movieWatchedService.getMovie(id);

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/movie")
    public ResponseEntity<?> getWatchedMovie(@RequestParam(value = "code") String code) {
        Optional<MovieDto> movie;

        try {
            movie = movieWatchedService.getMovie(code);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessages.INVALID_CODE_FORMAT, e);
        }

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieDto> createWatchedMovie(@Valid @RequestBody MovieDto movie) throws URISyntaxException {
        log.info("Request to add movie: {}", movie); // TODO: Fix unique constraint & return URI

        MovieDto result = movieWatchedService.createMovie(movie);

        return ResponseEntity.created(new URI("/api/watched/movie?code=" + result.getCode()))
                .body(result);
    }

    @PostMapping("/movieByUser")
    public ResponseEntity<MovieDto> createWatchedMovieByUser(@Valid @RequestBody MovieDto movie) throws URISyntaxException {
        log.info("Request to add movie: {}", movie); // TODO: Fix unique constraint & return URI

        MovieDto result = movieWatchedService.createMovieByUser(movie);

        return ResponseEntity.created(new URI("/api/watched/movie?code=" + result.getCode()))
                .body(result);
    }

    @PutMapping("/movie/{id}")
    public ResponseEntity<Optional<MovieDto>> updateWatchedMovie(@Valid @RequestBody MovieDto movie, @PathVariable int id) {
        log.info("Request to update movie by id: {}", movie);

        var result = movieWatchedService.updateMovie(id, movie);

        if (result.isPresent()) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/movie")
    public ResponseEntity<Optional<MovieDto>> updateWatchedMovie(@Valid @RequestBody MovieDto movie, @RequestParam(value = "code") String code) {
        log.info("Request to update movie by code: {}", movie);

        var result = movieWatchedService.updateMovie(code, movie);

        if (result.isPresent()) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteWatchedMovie(@PathVariable int id) {
        log.info("Request to delete movie by id: {}", id);

        movieWatchedService.deleteMovie(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/movie")
    public ResponseEntity<?> deleteWatchedMovie(@RequestParam(value = "code") String code) {
        log.info("Request to delete movie by code {}", code);

        movieWatchedService.deleteMovie(code);

        return ResponseEntity.ok().build();
    }

}
