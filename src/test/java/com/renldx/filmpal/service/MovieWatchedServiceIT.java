package com.renldx.filmpal.service;

import com.renldx.filmpal.entity.MovieDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieWatchedServiceIT {

    static OracleContainer oracle = new OracleContainer(
            "gvenzl/oracle-free"
    );

    @Autowired
    MovieWatchedService movieWatchedService;

    @BeforeAll
    static void beforeAll() {
        oracle.start();
    }

    @AfterAll
    static void afterAll() {
        oracle.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracle::getJdbcUrl);
        registry.add("spring.datasource.username", oracle::getUsername);
        registry.add("spring.datasource.password", oracle::getPassword);
    }

    @BeforeEach
    void setUp() {
        movieWatchedService.deleteMovies();
    }

    @Test
    void createAndGetMovieById_ReturnsMovie() {
        var movieInput = new MovieDto("TestMovie", new Date());
        movieWatchedService.createMovie(movieInput);

        var movieOutput = movieWatchedService.getMovie(1);
        assertThat(movieOutput).isNotNull();
    }

}
