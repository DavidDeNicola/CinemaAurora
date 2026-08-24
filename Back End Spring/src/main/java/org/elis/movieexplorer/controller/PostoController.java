package org.elis.movieexplorer.controller;

import java.util.List;

import org.elis.movieexplorer.dto.biglietto.response.ResponseBigliettoDTO;
import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySalaDTO;
import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySpettacoloDTO;
import org.elis.movieexplorer.service.definition.PostoService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostoController {
	private final PostoService postoService;
	
	
	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Trova i posti di una sala",
	    description = "Ritorna tutti i posti che hanno uno specifico ID di una sala",
	    responses = {
	       @ApiResponse(
	        	responseCode = "200",
	        	description = "Posti trovati",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponsePostoBySalaDTO[].class,
	        	        description = "Posti della sala"
	        	    )
	        	)
	        ),
	       @ApiResponse(
	        	responseCode = "204",
	        	description = "Nessun posto trovato",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseErroreDTO.class,
	        	        description = SwaggerTags.ERROR_TAG
	        	    )
	        	)
	       )
		}
	)
	@BadRequestApiResponse
	@GetMapping("/staff/posto/{idSala}")
	public ResponseEntity<List<ResponsePostoBySalaDTO>> findByIdSala(@PathVariable Long idSala) {
		return ResponseEntity.ok(postoService.findBySala(idSala));
	}
	
	
	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Trova i posti di uno spettacolo",
	    description = "Ritorna tutti i posti che hanno uno specifico ID di uno spettacolo",
	    responses = {
	       @ApiResponse(
	        	responseCode = "200",
	        	description = "Posti trovati",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponsePostoBySpettacoloDTO[].class,
	        	        description = "Posti dello spettacolo"
	        	    )
	        	)
	        ),
	       @ApiResponse(
	        	responseCode = "204",
	        	description = "Nessun posto trovato",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseErroreDTO.class,
	        	        description = SwaggerTags.ERROR_TAG
	        	    )
	        	)
	       )
		}
	)
	@BadRequestApiResponse
	@GetMapping("/cliente/posto/{idSpettacolo}")
	public ResponseEntity<List<ResponsePostoBySpettacoloDTO>> findByIdSpettacolo(@PathVariable Long idSpettacolo) {
		return ResponseEntity.ok(postoService.findBySpettacolo(idSpettacolo));
	}
	
}
