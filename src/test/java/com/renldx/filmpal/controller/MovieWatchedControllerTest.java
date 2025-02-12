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
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieWatchedController.class)
class MovieWatchedControllerTest {

    private static Collection<MovieDto> mockMovies;

    private static MovieDto mockMovie;

    private static String mockMovieJsonInput;
    private static String mockMovieJsonOutput;
    private static String mockMoviesJsonOutput;

    @MockitoBean
    private MovieWatchedService movieWatchedService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();

        var calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.JANUARY, 1);
        var mockDate = calendar.getTime();

        mockMovieJsonInput = "{\"title\":\"TestMovie\",\"release\":\"2001-01-01\"}";

        mockMovie = new MovieDto("TestMovie", mockDate);
        mockMovieJsonOutput = objectMapper.writeValueAsString(mockMovie);
        mockMovies = new ArrayList<>();
        mockMovies.add(mockMovie);
        mockMoviesJsonOutput = objectMapper.writeValueAsString(mockMovies);
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

            JSONAssert.assertEquals(mockMoviesJsonOutput, json, true);
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

            JSONAssert.assertEquals(mockMovieJsonOutput, json, true);
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
        void getWatchedMovieById_Null_ReturnsNotFound() throws Exception {
            when(movieWatchedService.getMovie(null)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/watched/movie/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
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

            JSONAssert.assertEquals(mockMovieJsonOutput, json, true);
        }

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2100-01-01"})
        void getWatchedMovieByCode_Invalid_ReturnsNotFound(String code) throws Exception {
            when(movieWatchedService.getMovie(code)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void getWatchedMovieByCode_Malformed_ReturnsBadRequest(String code) throws Exception {
            when(movieWatchedService.getMovie(code)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class createWatchedMovieTest {

        @Test
        void createWatchedMovie_Valid_ReturnsCreated() throws Exception {
            when(movieWatchedService.createMovie(any())).thenReturn(mockMovie);

            mockMvc.perform(post("/api/watched/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void createWatchedMovie_Invalid_ReturnsBadRequest(String content) throws Exception {
            mockMvc.perform(post("/api/watched/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createWatchedMovie_Existing_ReturnsUnprocessableEntity() throws Exception {
            when(movieWatchedService.createMovie(any())).thenThrow(DataIntegrityViolationException.class);

            mockMvc.perform(post("/api/watched/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

    }

    @Nested
    class updateWatchedMovieByIdTest {

        @ParameterizedTest
        @ValueSource(ints = {1})
        void updateWatchedMovieById_Valid_ReturnsOk(int id) throws Exception {
            when(movieWatchedService.updateMovie(eq(id), any())).thenReturn(Optional.ofNullable(mockMovie));

            mockMvc.perform(put("/api/watched/movie/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(ints = {0})
        void updateWatchedMovieById_Invalid_ReturnsNotFound(int id) throws Exception {
            when(movieWatchedService.updateMovie(eq(id), any())).thenReturn(Optional.empty());

            mockMvc.perform(put("/api/watched/movie/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(ints = {1})
        void updateWatchedMovieById_Malformed_ReturnsBadRequest(int id) throws Exception {
            mockMvc.perform(put("/api/watched/movie/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("\"xyz\""))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class updateWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001-01-01"})
        void updateWatchedMovieByCode_Valid_ReturnsOk(String code) throws Exception {
            when(movieWatchedService.updateMovie(eq(code), any())).thenReturn(Optional.ofNullable(mockMovie));

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2100-01-01"})
        void updateWatchedMovieByCode_Invalid_ReturnsNotFound(String code) throws Exception {
            when(movieWatchedService.updateMovie(eq(code), any())).thenReturn(Optional.empty());

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void updateWatchedMovieByCode_Malformed_Param_ReturnsBadRequest(String code) throws Exception {
            when(movieWatchedService.updateMovie(eq(code), any())).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieJsonInput))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void updateWatchedMovieByCode_Malformed_Body_ReturnsBadRequest(String content) throws Exception {
            mockMvc.perform(put("/api/watched/movie?code={code}", "TestMovie_2001-01-01")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class deleteWatchedMovieByIdTest {

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void deleteWatchedMovieById_ReturnsOk(int id) throws Exception {
            Mockito.doNothing().when(movieWatchedService).deleteMovie(id);

            mockMvc.perform(delete("/api/watched/movie/{id}", id))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void deleteWatchedMovieById_Null_ReturnsNotFound() throws Exception {
            mockMvc.perform(delete("/api/watched/movie/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    class deleteWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001-01-01", "TestMovie_2100-01-01"})
        void deleteWatchedMovieByCode_ReturnsOk(String code) throws Exception {
            Mockito.doNothing().when(movieWatchedService).deleteMovie(code);

            mockMvc.perform(delete("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void deleteWatchedMovieByCode_Malformed_ReturnsBadRequest(String code) throws Exception {
            Mockito.doThrow(IllegalArgumentException.class).when(movieWatchedService).deleteMovie(code);

            mockMvc.perform(delete("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }
}
