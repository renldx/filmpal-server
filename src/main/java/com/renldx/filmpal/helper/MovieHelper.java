package com.renldx.filmpal.helper;

import com.renldx.filmpal.constant.ExceptionMessages;
import com.renldx.filmpal.constant.Formats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieHelper {
    public static String getMovieCode(String title, Date release) {
        var encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        var simpleDate = new SimpleDateFormat(Formats.DATE_FORMAT).format(release);

        return String.format("%s_%s", encodedTitle, simpleDate);
    }

    public static String[] getMovieTitleAndRelease(String code) {
        var result = code.split("_");

        if (result.length != 2) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CODE_FORMAT);
        }

        return result;
    }

    public static Date getMovieRelease(String release) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(Formats.DATE_FORMAT);

        return formatter.parse(release);
    }
}
