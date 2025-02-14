package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.model.MovieDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieDetailsServiceIT {

    private static MovieDetailsService movieDetailsService;

    @BeforeAll
    public static void beforeAll() {
        movieDetailsService = new MovieDetailsService();
    }

    @ParameterizedTest
    @ValueSource(strings = {"tt1630029"})
    void getMovieDetails_ReturnsMovieDetails(String imdbId) throws ApiClientException {
        MovieDetails movieDetails = movieDetailsService.getMovieDetails(imdbId);
        assertThat(movieDetails).isNotNull();
    }

}
