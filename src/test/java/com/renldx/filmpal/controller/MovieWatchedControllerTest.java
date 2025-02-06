package com.renldx.filmpal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieWatchedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getWatchedMoviesTest {

        @Test
        void getWatchedMovies_ReturnsWatchedMovies() throws Exception {
            var result = mockMvc.perform(get("/api/watched/movies/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            assert (!json.isBlank()); // TODO: Add seed data
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
