package com.renldx.filmpal.server.helper;

import com.renldx.filmpal.server.constant.ExceptionMessages;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;

public class MovieHelper {

    public static String getMovieCode(String title, Year release) {
        var encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

        return String.format("%s_%s", encodedTitle, release);
    }

    public static String[] getMovieTitleAndRelease(String code) {
        var result = code.split("_");

        if (result.length != 2) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CODE_FORMAT);
        }

        return result;
    }

    public static Year getMovieRelease(String release) {
        return Year.parse(release);
    }

}
