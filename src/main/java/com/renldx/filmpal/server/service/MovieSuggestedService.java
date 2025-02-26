package com.renldx.filmpal.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renldx.filmpal.server.api.OpenAiClient;
import com.renldx.filmpal.server.model.GenreCode;
import com.renldx.filmpal.server.model.MovieDto;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MovieSuggestedService {

    private final OpenAiClient openAiClient;

    public MovieSuggestedService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    // TODO: Refactor collection type to set
    public Collection<MovieDto> getMovies(GenreCode genreCode, Collection<MovieDto> watchedMoviesList) throws JsonProcessingException {
        var response = openAiClient.getChatResponse(genreCode, watchedMoviesList);
        return response.movies().stream().map(m -> new MovieDto(m.title(), Year.parse(m.release()))).collect(Collectors.toList());
    }

}
