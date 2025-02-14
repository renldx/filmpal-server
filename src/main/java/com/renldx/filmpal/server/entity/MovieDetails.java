package com.renldx.filmpal.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public record MovieDetails(
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
        List<Rating> ratings,
        String metascore,
        String imdbRating,
        String imdbVotes,
        String imdbID,
        String type,
        String dvd,
        String boxOffice,
        String production,
        String website,
        String response
) {
    public record Rating(
            String source,
            String value
    ) {
    }
}
