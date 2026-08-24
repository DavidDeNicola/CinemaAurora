package org.elis.movieexplorer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.elis.movieexplorer.dto.biglietto.request.InsertBigliettoDTO;
import org.elis.movieexplorer.dto.biglietto.response.ResponseBigliettoDTO;
import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.service.definition.BigliettoService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BigliettoController {
	private final BigliettoService bigliettoService;
	
	@Tag(name = SwaggerTags.CUSTOMER_TAG, description = SwaggerTags.CUSTOMER_TAG_DESC)
    @Operation(
        summary = "Inserimento biglietti",
        description = "Il cliente compra i biglietti che vengono creati e aggiunti al DB",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Biglietti creati",
        		content = @Content(
        			mediaType = MediaType.APPLICATION_JSON_VALUE,
        			schema = @Schema(
        				implementation = ResponseBigliettoDTO[].class,
        				description = "Biglietti appena creati"
        			)
        		)
        	),
        	@ApiResponse(
            	responseCode = "404",
            	description = "Utente o Spettacolo non trovati",
            	content = @Content(
            		mediaType = MediaType.APPLICATION_JSON_VALUE,
            		schema = @Schema(
            			implementation = ResponseErroreDTO.class,
            			description = SwaggerTags.ERROR_TAG
            		)
            	)
            ),
        	@ApiResponse(
                responseCode = "422",
                description = "Biglietti esauriti oppure dati di input non validi",
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
	@PostMapping("/cliente/biglietto")
	public ResponseEntity<List<ResponseBigliettoDTO>> insert(@RequestBody @Valid InsertBigliettoDTO dto, Authentication authenticator){
		Utente u = (Utente) authenticator.getPrincipal();
		return ResponseEntity.ok(bigliettoService.insert(dto, u));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Trova i biglietti di un utente",
	    description = "Ritorna tutti i biglietti che hanno uno specifico ID di un utente",
	    responses = {
	       @ApiResponse(
	        	responseCode = "200",
	        	description = "Biglietti trovati",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseBigliettoDTO.class,
	        	        description = "Biglietti dell'utente"
	        	    )
	        	)
	        ),
	       @ApiResponse(
	        	responseCode = "204",
	        	description = "Nessun biglietto trovato",
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
	@GetMapping("/staff/biglietto/utente/{id}")
	public ResponseEntity<List<ResponseBigliettoDTO>> findByIdUtente(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID dell'utente di cui cercare i biglietti", 
					required = true, 
					allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(bigliettoService.findByIdUtente(id));
	}
	
	
	
	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Trova i biglietti venduti di uno spettacolo",
	    description = "Ritorna tutti i biglietti che hanno uno specifico ID di uno spettacolo",
	    responses = {
	        @ApiResponse(
	        	responseCode = "200",
	        	description = "Biglietti trovati",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseBigliettoDTO.class,
	        	        description = "Biglietti dello spettacolo"
	        	    )
	        	)
	        ),
	        @ApiResponse(
	        	responseCode = "404",
	        	description = "Nessun biglietto trovato",
	        	content = @Content(
	        	    mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseErroreDTO.class,
	        	        description = SwaggerTags.ERROR_TAG
	       	        )
	            )
	        ),
		}
	)
	@BadRequestApiResponse
	@GetMapping("/staff/biglietto/spettacolo/{id}")
	public ResponseEntity<List<ResponseBigliettoDTO>> findByIdSpettacolo(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID dello spettacolo di cui cercare i biglietti", 
					required = true, 
					allowEmptyValue = false) Long id){
		return ResponseEntity.ok(bigliettoService.findByIdSpettacolo(id));
	}
	
	
	
	@Tag(name = SwaggerTags.CUSTOMER_TAG, description = SwaggerTags.CUSTOMER_TAG_DESC)
	@Operation(
		summary = "Elimina un biglietto",
	    description = "Un utente può eliminare uno dei propri biglietti che ha uno specifico ID",
	    responses = {
	        @ApiResponse(
	        	responseCode = "200",
	        	description = "Biglietto eliminato"	        	     
	        ),
	        @ApiResponse(
	        	responseCode = "404",
	        	description = "Utente o biglietto non trovati",
	        	content = @Content(
	        		mediaType = MediaType.APPLICATION_JSON_VALUE,
	        	    schema = @Schema(
	        	        implementation = ResponseErroreDTO.class,
	        	        description = SwaggerTags.ERROR_TAG
	        	    )
	        	)
	        ),
	        @ApiResponse(
	        	responseCode = "422",
	        	description = "Non si può eliminare un biglietto per uno spettacolo già passato",
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
	@DeleteMapping("cliente/biglietto/{id}")
	public ResponseEntity<Void> removeById(
			@PathVariable @Parameter(
					name = "ID",
					description = "ID del biglietto da eliminare", 
					required = true, 
					allowEmptyValue = false) Long id, 
			@RequestBody String email){
		bigliettoService.removeByIdEmail(id, email);
		return ResponseEntity.ok().build();
	}
	
	
	
	
	
	@GetMapping("/cliente/biglietto")
	public ResponseEntity<List<ResponseBigliettoDTO>> getAll(Authentication authenticator){
		Utente u = (Utente) authenticator.getPrincipal();
		return ResponseEntity.ok(bigliettoService.findByIdUtente(u.getId()));
	}
	
	
	
	
	
	
	
}
