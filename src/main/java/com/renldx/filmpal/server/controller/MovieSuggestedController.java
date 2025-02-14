package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.model.Genres;
import com.renldx.filmpal.server.model.MovieDto;
import com.renldx.filmpal.server.service.MovieSuggestedService;
import com.renldx.filmpal.server.service.MovieWatchedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/suggested/")
public class MovieSuggestedController {

    private final Logger log = LoggerFactory.getLogger(MovieSuggestedController.class);
    private final MovieWatchedService movieWatchedService;
    private final MovieSuggestedService movieSuggestedService;

    public MovieSuggestedController(MovieWatchedService movieWatchedService, MovieSuggestedService movieSuggestedService) {
        this.movieWatchedService = movieWatchedService;
        this.movieSuggestedService = movieSuggestedService;
    }

    @GetMapping("/{genre}")
    public Collection<MovieDto> getSuggestedMovies(@PathVariable Genres genre) throws Exception {
        var watchedMovies = movieWatchedService.getMovies();
        return movieSuggestedService.getMovies(genre, watchedMovies);
    }

}
