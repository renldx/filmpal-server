package com.renldx.filmpal.controller;

import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.service.MovieWatchedService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<?> getWatchedMovieById(@PathVariable int id) {
        Optional<MovieDto> movie = movieWatchedService.getMovie(id);

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/movie")
    public ResponseEntity<?> getWatchedMovieByCode(@RequestParam(value = "code") String code) throws Exception {
        Optional<MovieDto> movie = movieWatchedService.getMovie(code); // TODO: Fix exception when not found

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieDto> addWatchedMovie(@Valid @RequestBody MovieDto movie) throws URISyntaxException {
        log.info("Request to add movie: {}", movie); // TODO Fix post/put handlers

        MovieDto result = movieWatchedService.saveMovie(movie);

        return ResponseEntity.created(new URI("/api/watched/movie" + result.getCode()))
                .body(result);
    }

    @PutMapping("/movie/{id}")
    public ResponseEntity<MovieDto> updateWatchedMovie(@Valid @RequestBody MovieDto movie, @PathVariable int id) throws Exception {
        log.info("Request to update movie by id: {}", movie);

        MovieDto result = movieWatchedService.saveMovie(id, movie);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/movie")
    public ResponseEntity<MovieDto> updateWatchedMovie(@Valid @RequestBody MovieDto movie) {
        log.info("Request to update movie by code: {}", movie);

        MovieDto result = movieWatchedService.saveMovie(movie);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteWatchedMovie(@PathVariable int id) {
        log.info("Request to delete movie by id: {}", id); // TODO: Fix return when movie doesn't exist

        movieWatchedService.deleteMovie(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/movie")
    public ResponseEntity<?> deleteWatchedMovie(@RequestParam(value = "code") String code) throws Exception {
        log.info("Request to delete movie by code: {}", code); // TODO: Fix exception when movie doesn't exist

        movieWatchedService.deleteMovie(code);

        return ResponseEntity.ok().build();
    }
}
