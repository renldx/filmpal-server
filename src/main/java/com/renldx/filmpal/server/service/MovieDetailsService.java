package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.helper.MovieHelper;
import com.renldx.filmpal.server.model.MovieDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MovieDetailsService {

    private final RestClient restClient;

    public MovieDetailsService() {
        this.restClient = RestClient.builder().baseUrl("http://www.omdbapi.com").build();
    }

    public MovieDetails getMovieDetails(String code) throws ApiClientException {
        try {
            var params = MovieHelper.getMovieTitleAndRelease(code);

            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", System.getenv("OMDB_API_KEY"))
                            .queryParam("t", params[0])
                            .queryParam("y", params[1])
                            .build())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(MovieDetails.class);
        } catch (Exception e) {
            throw new ApiClientException(e.getMessage());
        }
    }

}
