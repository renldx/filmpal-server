package com.renldx.filmpal.entity;

import com.renldx.filmpal.helpers.MovieHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class MovieDto {

    @Setter
    private String title;
    @Setter
    private Date release;
    private String code;

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
