package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.model.Genres;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/genres")
    public Collection<Genres> getGenres() {
        return List.of(Genres.values());
    }

}
