package com.renldx.filmpal.server.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Year;

public record MovieCreateRequest(

        @NotBlank
        String title,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Year release

) {
}
