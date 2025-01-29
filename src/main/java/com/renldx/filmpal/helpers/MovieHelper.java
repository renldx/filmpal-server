package com.renldx.filmpal.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MovieHelper {
    public static String GetMovieCode(String title, Date release) {
        var simpleDate = new SimpleDateFormat("yyyy-MM-dd").format(release);

        return String.format("%s_%s", title, simpleDate);
    }

    public static String[] GetMovieTitleAndRelease(String code) {
        var result = code.split("_");

        if (result.length != 2) {
            throw new IllegalArgumentException("Invalid movie code");
        }

        return result;
    }

    public static Date GetMovieRelease(String release) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.parse(release);
    }
}
