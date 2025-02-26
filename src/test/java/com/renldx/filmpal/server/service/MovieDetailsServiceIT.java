package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.payload.response.MovieDetailsResponse;
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
    @ValueSource(strings = {"Barbie_2023"})
    void getMovieDetails_ReturnsMovieDetails(String code) {
        MovieDetailsResponse movieDetailsResponse = movieDetailsService.getMovieDetailsByCode(code);
        assertThat(movieDetailsResponse).isNotNull();
    }

}
