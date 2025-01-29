package com.renldx.filmpal.controller;

import com.renldx.filmpal.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/suggest-movies/")
public class MovieSuggestedController {

    private final Logger log = LoggerFactory.getLogger(MovieSuggestedController.class);

    @GetMapping("/{genre}")
    public Collection<Movie> getSuggestedMovies(@PathVariable String genre) throws Exception {
        throw new Exception();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getSuggestedMovieDetails(@PathVariable String code) throws Exception {
        throw new Exception();
    }
}
