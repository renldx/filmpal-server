package com.renldx.filmpal.server.payload.response;

import java.util.Set;

public record JwtResponse(

        String token,

        String type,

        String username,

        Set<String> roles

) {
}
