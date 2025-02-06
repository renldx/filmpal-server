package com.renldx.filmpal.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieSuggestedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class getSuggestedMoviesTest {

        @ParameterizedTest
        @ValueSource(strings = {"ACTION"})
        void getSuggestedMovies_ReturnsMovies(String genre) throws Exception {
            var result = mockMvc.perform(get("/api/suggested/" + genre))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();

            assert (!json.isBlank());
        }

    }
}
