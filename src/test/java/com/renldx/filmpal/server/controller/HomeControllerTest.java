package com.renldx.filmpal.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.server.entity.Genres;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getGenresTest {

        @Test
        void getGenres_ReturnsGenres() throws Exception {
            var result = mockMvc.perform(get("/api/genres"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            var json = result.getResponse().getContentAsString();
            Genres[] genres = objectMapper.readValue(json, new TypeReference<>() {
            });

            assertIterableEquals(Arrays.asList(genres), Arrays.asList(Genres.values()));
        }

    }
}
