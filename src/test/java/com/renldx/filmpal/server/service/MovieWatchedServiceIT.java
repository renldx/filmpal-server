package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.helper.AuthHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.oracle.OracleContainer;

import java.time.Year;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieWatchedServiceIT {

    static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-free");

    private static String movieCode = "TestMovie_2001";

    @MockitoBean
    private AuthHelper authHelper;

    @Autowired
    private UserService userService;

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
    @Order(0)
    void createUser_CreatesUser() {
        userService.createUser("testUser", "testPassword", Set.of("USER"));
    }

    @Test
    @Order(1)
    void createMovie_ReturnsMovie() {
        when(authHelper.getUserId()).thenReturn(1L);

        var movieOutput = movieWatchedService.createUserMovie("TestMovie", Year.parse("2001"));

        assertThat(movieOutput).isNotNull();
    }

    @Test
    @Order(2)
    void getMovies_ReturnsMovies() {
        when(authHelper.getUserId()).thenReturn(1L);

        var movies = movieWatchedService.getUserMovies();

        assertThat(movies).isNotEmpty();
    }

    @Test
    @Order(2)
    void findMovieByCode_ReturnsMovie() {
        when(authHelper.getUserId()).thenReturn(1L);

        var movie = movieWatchedService.getUserMovie(movieCode);

        assertThat(movie).isNotNull();
    }

    @Test
    @Order(3)
    void updateMovieByCode_UpdatesMovie() {
        when(authHelper.getUserId()).thenReturn(1L);

        var movie = movieWatchedService.getUserMovie(movieCode);

        if (movie.isEmpty()) {
            fail();
        } else {
            var updatedMovie = movieWatchedService.updateUserMovie(movieCode, "TestMovieUpdate", movie.get().getRelease());

            movieCode = "TestMovieUpdate_2001";

            assertThat(updatedMovie.getTitle()).isEqualTo("TestMovieUpdate");
        }
    }

    @Test
    @Order(4)
    void deleteMovieByCode_DoesntThrowException() {
        when(authHelper.getUserId()).thenReturn(1L);
        assertDoesNotThrow(() -> movieWatchedService.deleteUserMovie(movieCode));
    }

}
