package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.entity.MovieDto;
import com.renldx.filmpal.server.service.MovieSuggestedService;
import com.renldx.filmpal.server.service.MovieWatchedService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieSuggestedController.class)
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
            when(movieWatchedService.getMovies()).thenReturn(new ArrayList<>());

            var mockMovie = new MovieDto("TestMovie", new Date());
            var mockSuggestedMovies = new ArrayList<MovieDto>();
            mockSuggestedMovies.add(mockMovie);

            when(movieSuggestedService.getMovies(any(), any())).thenReturn(mockSuggestedMovies);

            var result = mockMvc.perform(get("/api/suggested/" + genre))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            assert (!json.isBlank());
        }

        @ParameterizedTest
        @ValueSource(strings = {"XYZ"})
        void getSuggestedMovies_Invalid_ReturnsBadRequest(String genre) throws Exception {
            mockMvc.perform(get("/api/suggested/" + genre))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }
}
