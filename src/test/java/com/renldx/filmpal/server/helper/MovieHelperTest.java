package com.renldx.filmpal.server.helper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Year;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieHelperTest {

    @ParameterizedTest
    @CsvSource({"Don't Look Up,2021"})
    void getMovieCode_ReturnsMovieCode(String title, String release) {
        var movieCode = MovieHelper.getMovieCode(title, Year.parse(release));
        assertThat(movieCode).isEqualTo("Don%27t+Look+Up_2021");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Don%27t+Look+Up_2021"})
    void getMovieTitleAndRelease_ReturnsMovieTitleAndRelease(String code) {
        var movieTitleAndRelease = MovieHelper.getMovieTitleAndRelease(code);
        assertThat(movieTitleAndRelease[0]).isEqualTo("Don't Look Up");
        assertThat(movieTitleAndRelease[1]).isEqualTo("2021");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2021"})
    void getMovieRelease_ReturnsMovieRelease(String code) {
        var movieRelease = MovieHelper.getMovieRelease(code);
        assertThat(movieRelease).isEqualTo(Year.parse("2021"));
    }

}
