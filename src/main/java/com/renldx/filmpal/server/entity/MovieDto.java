package com.renldx.filmpal.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.renldx.filmpal.server.constant.Formats;
import com.renldx.filmpal.server.helper.MovieHelper;
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
    private String imdbId;

    public MovieDto() {
    }

    public MovieDto(String title, Date release) {
        this.title = title;
        this.release = release;
    }

    public MovieDto(String title, Date release, String imdbId) {
        this.title = title;
        this.release = release;
        this.imdbId = imdbId;
    }

    public String getCode() {
        return MovieHelper.getMovieCode(title, release);
    }
}
