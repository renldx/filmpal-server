package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.payload.response.MovieDetailsResponse;
import com.renldx.filmpal.server.service.MovieDetailsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieDetailsControllerTest {

    private static MovieDetailsResponse mockMovieDetailsResponse;

    @MockitoBean
    private MovieDetailsService movieDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        mockMovieDetailsResponse = Mockito.mock(MovieDetailsResponse.class);
    }

    @Nested
    class getMovieDetailsTest {

        @ParameterizedTest
        @ValueSource(strings = {"Barbie_2023"})
        void getMovieDetailsByCode_Valid_ReturnsMovieDetails(String code) throws Exception {
            when(movieDetailsService.getMovieDetailsByCode(code)).thenReturn(mockMovieDetailsResponse);
            when(mockMovieDetailsResponse.response()).thenReturn("True");

            mockMvc.perform(get("/api/details/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xyz"})
        void getMovieDetailsByCode_Invalid_ReturnsNotFound(String code) throws Exception {
            when(movieDetailsService.getMovieDetailsByCode(code)).thenReturn(mockMovieDetailsResponse);
            when(mockMovieDetailsResponse.response()).thenReturn("False");

            mockMvc.perform(get("/api/details/movie?code={code}", code))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

}
