package com.renldx.filmpal.controller;

import com.renldx.filmpal.entity.MovieDto;
import com.renldx.filmpal.service.MovieWatchedService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieWatchedController.class)
class MovieWatchedControllerTest {

    @MockitoBean
    private MovieWatchedService movieWatchedService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class getWatchedMoviesTest {

        @Test
        void getWatchedMovies_ReturnsWatchedMovies() throws Exception {
            var mockMovie = new MovieDto("TestMovie", new Date());
            var mockWatchedMovies = new ArrayList<MovieDto>();
            mockWatchedMovies.add(mockMovie);

            when(movieWatchedService.getMovies()).thenReturn(mockWatchedMovies);

            var result = mockMvc.perform(get("/api/watched/movies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            assert (!json.isBlank());
        }

    }

    @Nested
    class getWatchedMovieByIdTest {

    }

    @Nested
    class getWatchedMovieByCodeTest {

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
