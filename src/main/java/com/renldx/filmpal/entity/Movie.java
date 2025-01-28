package com.renldx.filmpal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "YEAR"}))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Getter
    @Setter
    @Column(name = "RELEASE", nullable = false)
    private Date release;

    public Movie() {
    }

    public Movie(Date release, String title) {
        this.release = release;
        this.title = title;
    }
}
