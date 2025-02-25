package com.renldx.filmpal.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "ROLES")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", nullable = false)
    private RoleCode name;

    public Role() {
    }

    public Role(RoleCode name) {
        this.name = name;
    }

}
