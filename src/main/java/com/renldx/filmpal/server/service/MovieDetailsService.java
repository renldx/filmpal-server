package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.constant.EnvironmentVariables;
import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.helper.MovieHelper;
import com.renldx.filmpal.server.payload.response.MovieDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MovieDetailsService {

    private final RestClient restClient;

    public MovieDetailsService() {
        this.restClient = RestClient.builder().baseUrl("http://www.omdbapi.com").build();
    }

    public MovieDetailsResponse getMovieDetailsByCode(String code) throws ApiClientException {
        try {
            var params = MovieHelper.getMovieTitleAndRelease(code, false);

            if (Objects.equals(params[1], "????")) {
                return getMovieDetailsByTitle(params[0]);
            } else {
                return getMovieDetailsByTitleAndRelease(params[0], params[1]);
            }
        } catch (Exception e) {
            throw new ApiClientException(e.getMessage());
        }
    }

    private MovieDetailsResponse getMovieDetailsByTitle(String title) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", System.getenv(EnvironmentVariables.OMDB_API_KEY))
                        .queryParam("t", title)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(MovieDetailsResponse.class);
    }

    private MovieDetailsResponse getMovieDetailsByTitleAndRelease(String title, String release) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", System.getenv(EnvironmentVariables.OMDB_API_KEY))
                        .queryParam("t", title)
                        .queryParam("y", release)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(MovieDetailsResponse.class);
    }

}
