package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.payload.response.MovieDetailsResponse;
import com.renldx.filmpal.server.service.MovieDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/details/")
public class MovieDetailsController {

    private final Logger log = LoggerFactory.getLogger(MovieDetailsController.class);
    private final MovieDetailsService movieDetailsService;

    public MovieDetailsController(MovieDetailsService movieDetailsService) {
        this.movieDetailsService = movieDetailsService;
    }

    @GetMapping("/movie")
    @Cacheable("movieDetails")
    public ResponseEntity<MovieDetailsResponse> getMovieDetails(@RequestParam(value = "code") String code) throws ApiClientException {
        var movieDetails = movieDetailsService.getMovieDetailsByCode(code);

        if (Objects.equals(movieDetails.response(), "False")) {
            return ResponseEntity.badRequest().body(movieDetails);
        } else {
            return ResponseEntity.ok(movieDetails);
        }
    }

}
