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
    public Set<MovieResponse> getWatchedUserMovies() {
        var userMovies = movieWatchedService.getUserMovies();
        return userMovies.stream().map(m -> new MovieResponse(m.getTitle(), m.getRelease())).collect(Collectors.toSet());
    }

    @GetMapping("/movie")
    public MovieResponse getWatchedUserMovie(@RequestParam(value = "code") String code) {
        var movie = movieWatchedService.getUserMovie(code).orElseThrow();
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieResponse> createWatchedUserMovie(@Valid @RequestBody MovieCreateRequest request) throws URISyntaxException {
        var movie = movieWatchedService.createUserMovie(request.title(), request.release());
        var response = new MovieResponse(movie.getTitle(), movie.getRelease());

        return ResponseEntity.created(new URI("/api/watched/movie?code=" + response.getCode())).body(response);
    }

    @PutMapping("/movie")
    public MovieResponse updateWatchedUserMovie(@Valid @RequestBody MovieUpdateRequest request, @RequestParam(value = "code") String code) {
        var movie = movieWatchedService.updateUserMovie(code, request.title(), request.release());
        return new MovieResponse(movie.getTitle(), movie.getRelease());
    }

    @DeleteMapping("/movie")
    public ResponseEntity<?> deleteWatchedUserMovie(@RequestParam(value = "code") String code) {
        movieWatchedService.deleteUserMovie(code);
        return ResponseEntity.ok().build();
    }

}
