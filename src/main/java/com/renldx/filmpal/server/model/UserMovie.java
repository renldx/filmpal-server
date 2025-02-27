package com.renldx.filmpal.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "USER_MOVIES")
public class UserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    @Setter
    @NotNull
    @Column(name = "USER_ID")
    private long userId;

    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    public UserMovie() {
    }

    public UserMovie(long userId, Movie movie) {
        this.userId = userId;
        this.movie = movie;
    }

}
