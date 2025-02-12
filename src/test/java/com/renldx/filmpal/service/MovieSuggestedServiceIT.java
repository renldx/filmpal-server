package com.renldx.filmpal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.api.OpenAiClient;
import com.renldx.filmpal.entity.Genres;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MovieSuggestedServiceIT {

    private static MovieSuggestedService movieSuggestedService;

    @BeforeAll
    public static void beforeAll() {
        movieSuggestedService = new MovieSuggestedService(new OpenAiClient(new ObjectMapper()));
    }

    @Test
    void getMovies_ReturnsMovies() throws JsonProcessingException {
        var suggestedMovies = movieSuggestedService.getMovies(Genres.ACTION, Collections.emptyList());
        assertFalse(suggestedMovies.isEmpty());
    }
}
