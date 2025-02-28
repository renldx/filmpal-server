package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.payload.request.MovieCreateRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieWatchedServiceIT {

    private final static int movieId = 1;

    static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-free");

    private static String movieCode = "TestMovie_2001";

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
        var movieInput = new MovieCreateRequest("TestMovie", Year.parse("2001"));
        var movieOutput = movieWatchedService.createMovie(movieInput.title(), movieInput.release());

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
    void findMovieByCode_ReturnsMovie() {
        var movie = movieWatchedService.findMovie(movieCode);
        assertThat(movie).isNotNull();
    }

    @Test
    @Order(3)
    void updateMovieById_UpdatesMovie() {
        var movie = movieWatchedService.getMovie(movieId);

        movie.setTitle("TestMovieUpdate");
        var updatedMovie = movieWatchedService.updateMovie(movieId, movie.getTitle(), movie.getRelease());

        movieCode = "TestMovieUpdate_2001";
        assertThat(updatedMovie.getTitle()).isEqualTo("TestMovieUpdate");
    }

    @Test
    @Order(4)
    void updateMovieByCode_UpdatesMovie() {
        var movie = movieWatchedService.findMovie(movieCode);

        if (movie.isEmpty()) {
            fail();
        } else {
            movie.get().setTitle("TestMovieUpdateAgain");
            var updatedMovie = movieWatchedService.updateMovie(movieId, movie.get().getTitle(), movie.get().getRelease());

            movieCode = "TestMovieUpdateAgain_2001";
            assertThat(updatedMovie.getTitle()).isEqualTo("TestMovieUpdateAgain");
        }
    }

    @Test
    @Order(5)
    void deleteMovieByCode_DoesntThrowException() {
        assertDoesNotThrow(() -> movieWatchedService.deleteMovie(movieCode));
    }

}
