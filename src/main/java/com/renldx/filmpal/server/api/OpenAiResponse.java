package com.renldx.filmpal.server.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class OpenAiResponse {
    public Collection<MovieResponse> movies;

    public static class MovieResponse {
        public String title;
        public Date release;
        public String imdbId;
    }
}
