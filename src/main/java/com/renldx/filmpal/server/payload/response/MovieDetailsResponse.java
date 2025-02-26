package com.renldx.filmpal.server.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Set;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public record MovieDetailsResponse(
        String title,
        String year,
        String rated,
        String released,
        String runtime,
        String genre,
        String director,
        String writer,
        String actors,
        String plot,
        String language,
        String country,
        String awards,
        String poster,
        Set<Rating> ratings,
        String metascore,
        String imdbRating,
        String imdbVotes,
        String imdbID,
        String type,
        String dvd,
        String boxOffice,
        String production,
        String website,
        String response,
        String error
) {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    public record Rating(
            String source,
            String value
    ) {
    }
}
