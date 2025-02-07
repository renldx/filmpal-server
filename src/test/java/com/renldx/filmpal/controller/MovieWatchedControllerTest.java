package com.renldx.filmpal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.service.MovieWatchedService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieWatchedController.class)
class MovieWatchedControllerTest {

    private static Collection<MovieDto> mockMovies;

    private static MovieDto mockMovie;

    private static String mockMovieJson;
    private static String mockMoviesJson;

    @MockitoBean
    private MovieWatchedService movieWatchedService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.JANUARY, 1);
        var mockDate = calendar.getTime();

        mockMovie = new MovieDto("TestMovie", mockDate);
        mockMovieJson = objectMapper.writeValueAsString(mockMovie);

        mockMovies = new ArrayList<MovieDto>();
        mockMovies.add(mockMovie);
        mockMoviesJson = objectMapper.writeValueAsString(mockMovies);
    }

    @Nested
    class getWatchedMoviesTest {

        @Test
        void getWatchedMovies_ReturnsWatchedMovies() throws Exception {
            when(movieWatchedService.getMovies()).thenReturn(mockMovies);

            var result = mockMvc.perform(get("/api/watched/movies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            JSONAssert.assertEquals(mockMoviesJson, json, true);
        }

    }

    @Nested
    class getWatchedMovieByIdTest {

        @ParameterizedTest
        @ValueSource(ints = {1})
        void getWatchedMovieById_Valid_ReturnsWatchedMovie(int id) throws Exception {
            when(movieWatchedService.getMovie(id)).thenReturn(Optional.ofNullable(mockMovie));

            var result = mockMvc.perform(get("/api/watched/movie/{id}", id))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            JSONAssert.assertEquals(mockMovieJson, json, true);
        }

        @ParameterizedTest
        @ValueSource(ints = {0})
        void getWatchedMovieById_Invalid_ReturnsNotFound(int id) throws Exception {
            when(movieWatchedService.getMovie(id)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/watched/movie/{id}", id))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void getWatchedMovieById_Null_ReturnsPreconditionFailed() throws Exception {
            when(movieWatchedService.getMovie(null)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/watched/movie/"))
                    .andDo(print())
                    .andExpect(status().isPreconditionFailed());
        }

    }

    @Nested
    class getWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001-01-01"})
        void getWatchedMovieByCode_Valid_ReturnsWatchedMovie(String code) throws Exception {
            when(movieWatchedService.getMovie(code)).thenReturn(Optional.of(mockMovie));

            var result = mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            JSONAssert.assertEquals(mockMovieJson, json, true);
        }

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2100-01-01"})
        void getWatchedMovieByCode_Invalid_ReturnsWatchedMovie(String code) throws Exception {
            when(movieWatchedService.getMovie(code)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void getWatchedMovieByCode_Malformed_ReturnsWatchedMovie(String code) throws Exception {
            when(movieWatchedService.getMovie(code)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class createWatchedMovieTest {

    }

    @Nested
    class updateWatchedMovieByIdTest {

    }

    @Nested
    class updateWatchedMovieByCodeTest {

    }

    @Nested
    class deleteWatchedMovieByIdTest {

    }

    @Nested
    class deleteWatchedMovieByCodeTest {

    }
}
