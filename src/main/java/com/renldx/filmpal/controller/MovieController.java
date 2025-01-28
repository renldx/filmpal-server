package com.renldx.filmpal.controller;

import com.renldx.filmpal.entity.Movie;
import com.renldx.filmpal.service.MovieService;
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
@RequestMapping("/api")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public Collection<Movie> getMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id) {
        Optional<Movie> movie = movieService.getMovieById(id);

        return movie.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.info("Request to add movie: {}", movie);

        Movie result = movieService.saveMovie(movie);

        return ResponseEntity.created(new URI("/api/movie/" + result.getId()))
                .body(result);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@Valid @RequestBody Movie movie) {
        log.info("Request to update movie: {}", movie);

        Movie result = movieService.saveMovie(movie);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable int id) {
        log.info("Request to delete movie: {}", id);

        movieService.deleteMovie(id);

        return ResponseEntity.ok().build();
    }
}
