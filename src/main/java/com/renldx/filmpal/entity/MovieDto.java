package com.renldx.filmpal.entity;

import com.renldx.filmpal.helpers.MovieHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class MovieDto {

    private final String code;
    @Setter
    private String title;
    @Setter
    private Date release;

    public MovieDto(String title, Date release) {
        this.title = title;
        this.release = release;
        code = MovieHelper.GetMovieCode(title, release);
    }

    public MovieDto(Movie movie) {
        this.title = movie.getTitle();
        this.release = movie.getRelease();
        code = MovieHelper.GetMovieCode(title, release);
    }
}
