package org.elis.movieexplorer.controller;

import java.util.List;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.film.request.EditFilmDTO;
import org.elis.movieexplorer.dto.film.request.InsertFilmDTO;
import org.elis.movieexplorer.dto.film.response.ResponseFilmDTO;
import org.elis.movieexplorer.dto.omdbapi.response.LongOmdbResponseApiDTO;
import org.elis.movieexplorer.service.definition.FilmService;
import org.elis.movieexplorer.service.omdb.OmdbService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class FilmController {
	private final FilmService filmService;

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
    @Operation(
        summary = "Inserimento film",
        description = "Lo staff inserisce un nuovo film che viene creato e aggiunto al DB",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Film creato",
        		content = @Content(
        			mediaType = MediaType.APPLICATION_JSON_VALUE,
        			 schema = @Schema(
        				implementation = ResponseFilmDTO.class,
        				description = "Film appena creato"
        			)
        		)
        	),
        	@ApiResponse(
            	responseCode = "409",
            	description = "Titolo film già esistente",
            	content = @Content(
            		mediaType = MediaType.APPLICATION_JSON_VALUE,
            		schema = @Schema(
            			implementation = ResponseErroreDTO.class,
            			description = SwaggerTags.ERROR_TAG
            		)
            	)
            ),
        	@ApiResponse(
    	        responseCode = "400",
    		    description = "Dati di input non validi",
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
	@PostMapping("/staff/film")
	public ResponseEntity<ResponseFilmDTO> insert(@RequestBody @Valid InsertFilmDTO dto) {
		return ResponseEntity.ok(filmService.insert(dto));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Restituisce tutti i film",
		description = "Un membro può vedere tutti i film nel DB",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Film trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseFilmDTO[].class,
						description = "Tutti i film nel DB"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessun film trovato",
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
	@GetMapping("/film")
	public ResponseEntity<List<ResponseFilmDTO>> findAll() {
		return ResponseEntity.ok(filmService.findAll());
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Restituisce un film",
		description = "Un membro dello staff può vedere un film cercandolo tramite ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Film trovato",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseFilmDTO.class,
						description = "Film con quell'ID"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Nessun film trovato",
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
	@GetMapping("/film/{id}")
	public ResponseEntity<ResponseFilmDTO> findById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del film da cercare", 
					required = true, 
					allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(filmService.findById(id));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Restituisce i film di un genere",
		description = "Un membro dello staff può vedere dei film cercandoli tramite l'ID di uno specifico genere",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Film trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseFilmDTO[].class,
						description = "Film con quel genere"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessun film trovato",
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
	@GetMapping("/film/genere/{id}")
	public ResponseEntity<List<ResponseFilmDTO>> findByIdGenere(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del genere di cui cercare i film", 
					required = true, 
					allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(filmService.findByIdGenere(id));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
    @Operation(
        summary = "Modifica film",
        description = "Lo staff modifica un film in base al suo ID",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Film modificato",
        		content = @Content(
        			mediaType = MediaType.APPLICATION_JSON_VALUE,
        			schema = @Schema(
        				implementation = ResponseFilmDTO.class,
        				description = "Film con nuove modifiche"
        			)
        		)
        	),
        	@ApiResponse(
            	responseCode = "404",
            	description = "Film o generi non trovati",
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
                description = "Titolo film già esistente",
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
	@PatchMapping("/staff/film/{id}")
	public ResponseEntity<ResponseFilmDTO> editById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del film da modificare", 
					required = true, 
					allowEmptyValue = false) Long id, 
			@RequestBody EditFilmDTO dto) {
		return ResponseEntity.ok(filmService.editById(id, dto));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
    @Operation(
        summary = "Elimina film",
        description = "Lo staff elimina un film in base al suo ID",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
        		description = "Film rimosso"
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
	@DeleteMapping("/staff/film/{id}")
	public ResponseEntity<Void> removeById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID del film da rimuovere", 
					required = true, 
					allowEmptyValue = false) Long id) {
		filmService.removeById(id);
		return ResponseEntity.ok().build();
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
			summary = "Restituisce tutti i film che hanno un determinato nome",
			description = "Un membro dello staff può vedere i dettagli di uno o più film in base al nome.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Film trovati",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = LongOmdbResponseApiDTO[].class,
											description = "Le informazioni dei film"
									)
							)
					),
					@ApiResponse(
						responseCode = "203",
						description = "Non ci sono film con il nome inserito",
						content = @Content(
								mediaType = MediaType.APPLICATION_JSON_VALUE,
								schema = @Schema(
										implementation = LongOmdbResponseApiDTO[].class,
										description = "Nessun contenuto"
								)
						)
					),
					@ApiResponse(
							responseCode = "401",
							description = "Nessuna autorizzazione",
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
	@GetMapping("/film/titolo")
	public ResponseEntity<List<LongOmdbResponseApiDTO>> findByTitolo(@RequestParam String titolo){
		return ResponseEntity.ok().body(filmService.findByTitolo(titolo));
	}
	
	@GetMapping("/staff/film/check-exists")
	public ResponseEntity<Boolean> findByIMDBID(@RequestParam String id){
		
		return ResponseEntity.ok(filmService.findByImdbID(id));
	}
	
	
}