package com.renldx.filmpal.server.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponse {

    @Getter
    private final List<String> roles;

    private String token;

    private String type = "Bearer";

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

}
