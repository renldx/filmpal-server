package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.model.GenreCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/")
public class GenreController {

    private final Logger log = LoggerFactory.getLogger(GenreController.class);

    @GetMapping("/genres")
    public Set<GenreCode> getGenres() {
        return Set.of(GenreCode.values());
    }

}
