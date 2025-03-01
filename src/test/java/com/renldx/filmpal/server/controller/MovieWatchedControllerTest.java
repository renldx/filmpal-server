package com.renldx.filmpal.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.renldx.filmpal.server.model.Movie;
import com.renldx.filmpal.server.payload.request.MovieCreateRequest;
import com.renldx.filmpal.server.payload.request.MovieUpdateRequest;
import com.renldx.filmpal.server.payload.response.MovieResponse;
import com.renldx.filmpal.server.service.MovieWatchedService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieWatchedController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieWatchedControllerTest {

    private static Movie mockMovie;
    private static Set<Movie> mockMovies;

    private static String mockMovieCreateRequestJson;
    private static String mockMovieUpdateRequestJson;

    private static String mockMovieResponseJson;
    private static String mockMoviesResponseJson;

    @MockitoBean
    private MovieWatchedService movieWatchedService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var mockTitle = "TestMovie";
        var mockRelease = Year.parse("2001");

        mockMovie = new Movie(mockTitle, mockRelease);
        mockMovies = Set.of(mockMovie);

        var mockMovieCreateRequest = new MovieCreateRequest(mockTitle, mockRelease);
        mockMovieCreateRequestJson = objectMapper.writeValueAsString(mockMovieCreateRequest);

        var mockMovieUpdateRequest = new MovieUpdateRequest(mockTitle, mockRelease);
        mockMovieUpdateRequestJson = objectMapper.writeValueAsString(mockMovieUpdateRequest);

        var mockMovieResponse = new MovieResponse(mockTitle, mockRelease);
        mockMovieResponseJson = objectMapper.writeValueAsString(mockMovieResponse);

        var mockMoviesResponse = Set.of(mockMovieResponse);
        mockMoviesResponseJson = objectMapper.writeValueAsString(mockMoviesResponse);
    }

    @Nested
    class getWatchedMoviesTest {

        @Test
        void getWatchedMovies_ReturnsWatchedMovies() throws Exception {
            when(movieWatchedService.getUserMovies()).thenReturn(mockMovies);

            var result = mockMvc.perform(get("/api/watched/movies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            JSONAssert.assertEquals(mockMoviesResponseJson, json, true);
        }

    }

    @Nested
    class getWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001-01-01"})
        void getWatchedMovieByCode_Valid_ReturnsWatchedMovie(String code) throws Exception {
            when(movieWatchedService.getUserMovie(code)).thenReturn(Optional.ofNullable(mockMovie));

            var result = mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            JSONAssert.assertEquals(mockMovieResponseJson, json, true);
        }

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2100-01-01"})
        void getWatchedMovieByCode_Invalid_ReturnsNotFound(String code) throws Exception {
            when(movieWatchedService.getUserMovie(code)).thenThrow(NoSuchElementException.class);

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void getWatchedMovieByCode_Malformed_ReturnsBadRequest(String code) throws Exception {
            when(movieWatchedService.getUserMovie(code)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(get("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class createWatchedMovieTest {

        @Test
        void createWatchedMovie_Valid_ReturnsCreated() throws Exception {
            when(movieWatchedService.createUserMovie(any(), any())).thenReturn(mockMovie);

            mockMvc.perform(post("/api/watched/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieCreateRequestJson))
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
            when(movieWatchedService.createUserMovie(any(), any())).thenThrow(DataIntegrityViolationException.class);

            mockMvc.perform(post("/api/watched/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieCreateRequestJson))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

    }

    @Nested
    class updateWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001"})
        void updateWatchedMovieByCode_Valid_ReturnsOk(String code) throws Exception {
            when(movieWatchedService.updateUserMovie(eq(code), any(), any())).thenReturn(mockMovie);

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieUpdateRequestJson))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2100"})
        void updateWatchedMovieByCode_Invalid_ReturnsNotFound(String code) throws Exception {
            when(movieWatchedService.updateUserMovie(eq(code), any(), any())).thenThrow(NoSuchElementException.class);

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieUpdateRequestJson))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void updateWatchedMovieByCode_Malformed_Param_ReturnsBadRequest(String code) throws Exception {
            when(movieWatchedService.updateUserMovie(eq(code), any(), any())).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(put("/api/watched/movie?code={code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mockMovieUpdateRequestJson))
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
    class deleteWatchedMovieByCodeTest {

        @ParameterizedTest
        @ValueSource(strings = {"TestMovie_2001-01-01", "TestMovie_2100-01-01"})
        void deleteWatchedMovieByCode_ReturnsOk(String code) throws Exception {
            Mockito.doNothing().when(movieWatchedService).deleteUserMovie(code);

            mockMvc.perform(delete("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void deleteWatchedMovieByCode_Malformed_ReturnsBadRequest(String code) throws Exception {
            Mockito.doThrow(IllegalArgumentException.class).when(movieWatchedService).deleteUserMovie(code);

            mockMvc.perform(delete("/api/watched/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

}
