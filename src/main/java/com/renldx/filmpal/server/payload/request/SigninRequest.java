package com.renldx.filmpal.server.payload.request;

import jakarta.validation.constraints.NotBlank;

public record SigninRequest(

        @NotBlank
        String username,

        @NotBlank
        String password

) {
}
