package com.renldx.filmpal.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renldx.filmpal.server.api.OpenAiClient;
import com.renldx.filmpal.server.model.GenreCode;
import com.renldx.filmpal.server.model.Movie;
import com.renldx.filmpal.server.payload.response.MovieResponse;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieSuggestedService {

    private final OpenAiClient openAiClient;

    public MovieSuggestedService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public Set<MovieResponse> getMovies(GenreCode genreCode, Set<Movie> watchedMovies) throws JsonProcessingException {
        var response = openAiClient.getChatResponse(genreCode, watchedMovies);
        return response.movies().stream().map(m -> new MovieResponse(m.title(), Year.parse(m.release()))).collect(Collectors.toSet());
    }

}
