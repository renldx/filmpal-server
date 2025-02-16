package com.renldx.filmpal.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.renldx.filmpal.server.helper.MovieHelper;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
public class MovieDto {

    @NotNull
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Year release;

    public MovieDto() {
    }

    public MovieDto(String title, Year release) {
        this.title = title;
        this.release = release;
    }

    public String getCode() {
        return MovieHelper.getMovieCode(title, release);
    }

}
