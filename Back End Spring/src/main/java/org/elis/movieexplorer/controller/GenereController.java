package org.elis.movieexplorer.controller;

import java.util.List;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.genere.request.EditGenereDTO;
import org.elis.movieexplorer.dto.genere.request.InsertGenereDTO;
import org.elis.movieexplorer.dto.genere.response.ResponseGenereDTO;
import org.elis.movieexplorer.service.definition.GenereService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GenereController {
	private final GenereService genereService;

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
	    summary = "Inserimento genere",
	    description = "Lo staff inserisce un nuovo genere che viene creato e aggiunto al DB",
	    responses = {
	        @ApiResponse(
	        	responseCode = "200",
	        	description = "Genere creato",
	        	content = @Content(
	        		mediaType = MediaType.APPLICATION_JSON_VALUE,
	        		schema = @Schema(
	        			implementation = ResponseGenereDTO.class,
	        			description = "Genere appena creato"
	        		)
	        	)
	        ),
	        @ApiResponse(
	            responseCode = "409",
	            description = "Nome genere già esistente",
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
	@PostMapping("/staff/genere")
	public ResponseEntity<ResponseGenereDTO> insert(@RequestBody @Valid InsertGenereDTO dto) {
		return ResponseEntity.ok(genereService.insert(dto));
	}

	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce tutti i generi",
		description = "L'utente può vedere tutti i generi nel DB",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Generi trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseGenereDTO[].class,
						description = "Tutti i generi nel DB"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessun genere trovato",
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
	@GetMapping("/genere")
	public ResponseEntity<List<ResponseGenereDTO>> findAll() {
		return ResponseEntity.ok(genereService.findAll());
	}
	
	
	
	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce un genere",
		description = "L'utente può vedere un genere cercandolo tramite ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Genere trovato",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseGenereDTO.class,
						description = "Genere con quell'ID"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Nessun genere trovato",
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
	@GetMapping("/genere/{id}")
	public ResponseEntity<ResponseGenereDTO> findById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del genere da cercare", 
					required = true, 
					allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(genereService.findById(id));
	}

	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce i generi di un film",
		description = "L'utente può vedere i generi di un film cercandoli tramite l'ID di uno specifico film",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Generi trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseGenereDTO[].class,
						description = "Generi di quel film"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessun genere trovato",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseErroreDTO.class,
						description = SwaggerTags.ERROR_TAG
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Film non trovato",
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
	@GetMapping("/genere/film/{id}")
	public ResponseEntity<List<ResponseGenereDTO>> findByIdFilm(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del film di cui cercare i generi", 
					required = true, 
					allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(genereService.findByIdFilm(id));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
    @Operation(
        summary = "Modifica genere",
        description = "Lo staff modifica un genere in base al suo ID",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Genere modificato",
        		content = @Content(
        			mediaType = MediaType.APPLICATION_JSON_VALUE,
        			schema = @Schema(
        				implementation = ResponseGenereDTO.class,
        				description = "Genere con nuove modifiche"
        			)
        		)
        	),
        	@ApiResponse(
            	responseCode = "404",
            	description = "Genere o film non trovati",
            	content = @Content(
            		mediaType = MediaType.APPLICATION_JSON_VALUE,
            		schema = @Schema(
            			implementation = ResponseErroreDTO.class,
            			description = SwaggerTags.ERROR_TAG
            		)
            	)
            ),
        	@ApiResponse(
        		responseCode = "409",
                description = "Nome genere già esistente",
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
	@PatchMapping("/staff/genere/{id}")
	public ResponseEntity<ResponseGenereDTO> editById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del genere da modificare", 
					required = true, 
					allowEmptyValue = false) Long id, 
			@RequestBody EditGenereDTO dto) {
		return ResponseEntity.ok(genereService.editById(id, dto));
	}
	
	
	
	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
    @Operation(
        summary = "Elimina genere",
        description = "Lo staff elimina un genere in base al suo ID",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Genere rimosso"
        	),
        	@ApiResponse(
            	responseCode = "404",
            	description = "Genere non trovato",
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
	@DeleteMapping("/staff/genere/{id}")
	public ResponseEntity<Void> removeById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del genere da eliminare", 
					required = true, 
					allowEmptyValue = false) Long id) {
		genereService.removeById(id);
		return ResponseEntity.ok().build();
	}
}
