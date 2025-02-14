package com.renldx.filmpal.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renldx.filmpal.server.api.OpenAiClient;
import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.model.Genre;
import com.renldx.filmpal.server.model.MovieDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MovieSuggestedService {

    private final OpenAiClient openAiClient;

    public MovieSuggestedService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public Collection<MovieDto> getMovies(Genre genre, Collection<MovieDto> watchedMoviesList) throws JsonProcessingException, ApiClientException {
        var response = openAiClient.getChatResponse(genre, watchedMoviesList);
        return response.movies().stream().map(m -> new MovieDto(m.title(), m.release(), m.imdbId())).collect(Collectors.toList());
    }

}
