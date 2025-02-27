package com.renldx.filmpal.server.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.renldx.filmpal.server.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final String username;

    @JsonIgnore
    private String password;

    private final Set<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String password,
                           Set<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

}
