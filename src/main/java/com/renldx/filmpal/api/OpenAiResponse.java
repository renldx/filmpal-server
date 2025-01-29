package com.renldx.filmpal.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OpenAiResponse {
    public static class MovieResponse {
        public String title;
        public Date release;
    }
}
