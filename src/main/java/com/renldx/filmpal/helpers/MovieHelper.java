package com.renldx.filmpal.helpers;

import com.renldx.filmpal.constants.ExceptionMessages;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieHelper {
    public static String GetMovieCode(String title, Date release) {
        var encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        var simpleDate = new SimpleDateFormat("yyyy-MM-dd").format(release);

        return String.format("%s_%s", encodedTitle, simpleDate);
    }

    public static String[] GetMovieTitleAndRelease(String code) {
        var result = code.split("_");

        if (result.length != 2) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CODE_FORMAT);
        }

        return result;
    }

    public static Date GetMovieRelease(String release) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.parse(release);
    }
}
