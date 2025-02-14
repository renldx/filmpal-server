package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.entity.MovieDetails;
import com.renldx.filmpal.server.service.MovieDetailsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieDetailsController.class)
class MovieDetailsControllerTest {

    private static MovieDetails mockMovieDetails;

    @MockitoBean
    private MovieDetailsService movieDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        mockMovieDetails = Mockito.mock(MovieDetails.class);
    }

    @Nested
    class getMovieDetailsTest {

        @ParameterizedTest
        @ValueSource(strings = {"tt1234567", "tt12345678"})
        void getMovieDetails_Valid_ReturnsMovieDetails(String imdbId) throws Exception {
            when(movieDetailsService.getMovieDetails(imdbId)).thenReturn(mockMovieDetails);

            mockMvc.perform(get("/api/details/movie?imdbId={imdbId}", imdbId))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"tt0000000", "tt00000000"})
        void getMovieDetails_Invalid_ReturnsNotFound() throws Exception {
            // TODO
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void getMovieDetails_Malformed_ReturnsBadRequest() throws Exception {
            // TODO
        }

    }

}
