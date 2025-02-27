package com.renldx.filmpal.server.payload.response;

import java.util.Set;

public record JwtResponse(

        String type,

        String token,

        String username,

        Set<String> roles

) {
}
