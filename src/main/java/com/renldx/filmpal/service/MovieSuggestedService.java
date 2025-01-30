package com.renldx.filmpal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renldx.filmpal.api.OpenAiClient;
import com.renldx.filmpal.entity.Genres;
import com.renldx.filmpal.entity.MovieDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MovieSuggestedService {
    private final OpenAiClient openAiClient;

    public MovieSuggestedService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public Collection<MovieDto> getMovies(Genres genre, Collection<MovieDto> watchedMoviesList) throws JsonProcessingException {
        var response = openAiClient.getResponse(genre, watchedMoviesList);
        return response.movies.stream().map(m -> new MovieDto(m.title, m.release)).collect(Collectors.toList());
    }
}
