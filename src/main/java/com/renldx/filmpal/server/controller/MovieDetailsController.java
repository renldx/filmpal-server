package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.entity.MovieDetails;
import com.renldx.filmpal.server.service.MovieDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/details/")
public class MovieDetailsController {

    private final Logger log = LoggerFactory.getLogger(MovieDetailsController.class);
    private final MovieDetailsService movieDetailsService;

    public MovieDetailsController(MovieDetailsService movieDetailsService) {
        this.movieDetailsService = movieDetailsService;
    }

    @GetMapping("/movie")
    public MovieDetails getMovieDetails(@RequestParam(value = "imdbId") String imdbId) {
        return movieDetailsService.getMovieDetails(imdbId);
    }
}
