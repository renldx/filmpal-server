package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.entity.MovieDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MovieDetailsService {
    private final RestClient restClient;

    public MovieDetailsService(RestClient restClient) {
        this.restClient = restClient;
    }

    public MovieDetails getMovieDetails(String imdbId) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", System.getenv("OMDB_API_KEY"))
                        .queryParam("i", imdbId)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(MovieDetails.class);
    }
}
