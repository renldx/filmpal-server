package com.renldx.filmpal.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "RELEASE"}))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @NotBlank
    @Column(name = "TITLE")
    private String title;

    @Setter
    @NotBlank
    @Column(name = "RELEASE")
    private Year release;

    public Movie() {
    }

    public Movie(String title, Year release) {
        this.title = title;
        this.release = release;
    }

}
