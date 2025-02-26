package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.model.MovieDto;
import com.renldx.filmpal.server.service.MovieSuggestedService;
import com.renldx.filmpal.server.service.MovieWatchedService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieSuggestedController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieSuggestedControllerTest {

    @MockitoBean
    private MovieWatchedService movieWatchedService;

    @MockitoBean
    private MovieSuggestedService movieSuggestedService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class getSuggestedMoviesTest {

        @ParameterizedTest
        @ValueSource(strings = {"ACTION"})
        void getSuggestedMovies_Valid_ReturnsSuggestedMovies(String genre) throws Exception {
            when(movieWatchedService.getMovies()).thenReturn(Collections.emptySet());

            var mockMovie = new MovieDto("TestMovie", Year.parse("2001"));

            when(movieSuggestedService.getMovies(any(), any())).thenReturn(List.of(mockMovie));

            mockMvc.perform(get("/api/suggested/{genre}", genre))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"XYZ"})
        void getSuggestedMovies_Invalid_ReturnsBadRequest(String genre) throws Exception {
            mockMvc.perform(get("/api/suggested/{genre}", genre))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getSuggestedMovies_Null_ReturnsNotFound() throws Exception {
            mockMvc.perform(get("/api/suggested/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

    }

}
