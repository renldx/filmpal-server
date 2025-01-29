package com.renldx.filmpal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "YEAR"}))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Setter
    @Column(name = "RELEASE", nullable = false)
    private Date release;

    public Movie() {
    }

    public Movie(String title, Date release) {
        this.title = title;
        this.release = release;
    }
}
