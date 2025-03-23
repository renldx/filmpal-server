package com.renldx.filmpal.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getGenresTest {

//        @Test
//        void getGenres_ReturnsGenres() throws Exception {
//            var result = mockMvc.perform(get("/api/genres"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andReturn();
//
//            var json = result.getResponse().getContentAsString();
//            Set<GenreCode> genreCodes = objectMapper.readValue(json, new TypeReference<>() {
//            });
//
//            assertEquals(genreCodes, Set.of(GenreCode.values()));
//        }

    }

}
