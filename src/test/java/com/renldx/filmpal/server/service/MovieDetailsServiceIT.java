package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.entity.MovieDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieDetailsServiceIT {

    private static MovieDetailsService movieDetailsService;

    @BeforeAll
    public static void beforeAll() {
        var restClient = RestClient.builder().baseUrl("http://www.omdbapi.com").build();
        movieDetailsService = new MovieDetailsService(restClient);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tt1630029"})
    void getMovieDetails_ReturnsMovieDetails(String imdbId) {
        MovieDetails movieDetails = movieDetailsService.getMovieDetails(imdbId);
        assertThat(movieDetails).isNotNull();
    }

}
