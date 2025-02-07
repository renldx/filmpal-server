package com.renldx.filmpal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.renldx.filmpal.constants.Formats;
import com.renldx.filmpal.helpers.MovieHelper;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MovieDto {

    @NotNull
    private String title;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE_FORMAT)
    private Date release;

    public MovieDto() {
    }

    public MovieDto(String title, Date release) {
        this.title = title;
        this.release = release;
    }

    public MovieDto(Movie movie) {
        this.title = movie.getTitle();
        this.release = movie.getRelease();
    }

    public String getCode() {
        return MovieHelper.GetMovieCode(title, release);
    }
}
