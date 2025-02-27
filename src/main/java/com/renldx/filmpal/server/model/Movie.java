package com.renldx.filmpal.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "RELEASE"}))
public class Movie {

    @OneToMany(mappedBy = "movie")
    Set<UserMovie> userMovies = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @NotBlank
    @Column(name = "TITLE")
    private String title;

    @Setter
    @NotNull
    @Column(name = "RELEASE")
    private Year release;

    public Movie() {
    }

    public Movie(String title, Year release) {
        this.title = title;
        this.release = release;
    }

}
