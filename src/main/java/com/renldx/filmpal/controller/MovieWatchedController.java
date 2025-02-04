package com.renldx.filmpal.controller;

import com.renldx.filmpal.constants.ExceptionMessages;
import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.service.MovieWatchedService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/watched/")
public class MovieWatchedController {

    private final Logger log = LoggerFactory.getLogger(MovieWatchedController.class);
    private final MovieWatchedService movieWatchedService;

    public MovieWatchedController(MovieWatchedService movieWatchedService) {
        this.movieWatchedService = movieWatchedService;
    }

    @GetMapping("/movies")
    public Collection<MovieDto> getWatchedMovies() {
        return movieWatchedService.getMovies();
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getWatchedMovie(@PathVariable int id) {
        Optional<MovieDto> movie = movieWatchedService.getMovie(id);

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/movie")
    public ResponseEntity<?> getWatchedMovie(@RequestParam(value = "code") String code) throws Exception {
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
    public ResponseEntity<MovieDto> addWatchedMovie(@Valid @RequestBody MovieDto movie) throws URISyntaxException {
        log.info("Request to add movie: {}", movie); // TODO: Fix unique constraint & return URI

        MovieDto result = movieWatchedService.createMovie(movie);

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
    public ResponseEntity<Optional<MovieDto>> updateWatchedMovie(@Valid @RequestBody MovieDto movie, @RequestParam(value = "code") String code) throws ParseException {
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
    public ResponseEntity<?> deleteWatchedMovie(@RequestParam(value = "code") String code) throws ParseException {
        log.info("Request to delete movie by code {}", code);

        movieWatchedService.deleteMovie(code);

        return ResponseEntity.ok().build();
    }
}
