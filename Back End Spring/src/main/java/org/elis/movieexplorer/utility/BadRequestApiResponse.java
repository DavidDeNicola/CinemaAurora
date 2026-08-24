package org.elis.movieexplorer.utility;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    responseCode = "400",
    description = "Richiesta non valida. I dati forniti sono errati o incompleti.",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(
            implementation = ResponseErroreDTO.class,
            description = "I dati inviati sono null o malformati."
        )
    )
)
public @interface BadRequestApiResponse {}
