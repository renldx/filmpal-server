package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.entity.MovieDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieWatchedServiceIT {

    private final static int movieId = 1;

    static OracleContainer oracleContainer = new OracleContainer(
            "gvenzl/oracle-free"
    );

    private static String movieCode;

    @Autowired
    private MovieWatchedService movieWatchedService;

    @BeforeAll
    static void beforeAll() {
        oracleContainer.start();
    }

    @AfterAll
    static void afterAll() {
        oracleContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracleContainer::getJdbcUrl);
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
    }

    @Test
    @Order(1)
    void createMovie_ReturnsMovie() {
        var movieInput = new MovieDto("TestMovie", new Date());
        var movieOutput = movieWatchedService.createMovie(movieInput);

        movieCode = movieOutput.getCode();
        assertThat(movieOutput).isNotNull();
    }

    @Test
    @Order(2)
    void getMovies_ReturnsMovies() {
        var movies = movieWatchedService.getMovies();
        assertThat(movies).isNotEmpty();
    }

    @Test
    @Order(2)
    void getMovieById_ReturnsMovie() {
        var movie = movieWatchedService.getMovie(movieId);
        assertThat(movie).isNotNull();
    }

    @Test
    @Order(2)
    void getMovieByCode_ReturnsMovie() throws Exception {
        var movie = movieWatchedService.getMovie(movieCode);
        assertThat(movie).isNotNull();
    }

    @Test
    @Order(3)
    void updateMovieById_UpdatesMovie() {
        var movie = movieWatchedService.getMovie(movieId);

        if (movie.isEmpty()) {
            fail();
        } else {
            movie.get().setTitle("TestMovieUpdate");
            var updatedMovie = movieWatchedService.updateMovie(movieId, movie.get());

            if (updatedMovie.isEmpty()) {
                fail();
            } else {
                movieCode = updatedMovie.get().getCode();
                assertThat(updatedMovie.get().getTitle()).isEqualTo("TestMovieUpdate");
            }
        }
    }

    @Test
    @Order(4)
    void updateMovieByCode_UpdatesMovie() throws Exception {
        var movie = movieWatchedService.getMovie(movieCode);

        if (movie.isEmpty()) {
            fail();
        } else {
            movie.get().setTitle("TestMovieUpdateAgain");
            var updatedMovie = movieWatchedService.updateMovie(movieId, movie.get());

            if (updatedMovie.isEmpty()) {
                fail();
            } else {
                movieCode = updatedMovie.get().getCode();
                assertThat(updatedMovie.get().getTitle()).isEqualTo("TestMovieUpdateAgain");
            }
        }
    }

    @Test
    @Order(5)
    void deleteMovieByCode_DoesntThrowException() {
        assertDoesNotThrow(() -> movieWatchedService.deleteMovie(movieCode));
    }

}
