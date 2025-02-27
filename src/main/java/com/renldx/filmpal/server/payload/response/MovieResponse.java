package com.renldx.filmpal.server.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.renldx.filmpal.server.helper.MovieHelper;

import java.time.Year;

public record MovieResponse(

        String title,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Year release
) {
    public String getCode() {
        return MovieHelper.getMovieCode(title, release);
    }
}
