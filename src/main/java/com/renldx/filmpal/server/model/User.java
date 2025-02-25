package com.renldx.filmpal.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(columnNames = {"USERNAME", "EMAIL"}))
public class User {

    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @NotBlank
    @Column(name = "USERNAME")
    private String username;

    @Setter
    @NotBlank
    @Column(name = "EMAIL")
    private String email;

    @Setter
    @NotBlank
    @Column(name = "PASSWORD")
    private String password;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
