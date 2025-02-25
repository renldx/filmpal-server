package com.renldx.filmpal.server.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SigninRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
