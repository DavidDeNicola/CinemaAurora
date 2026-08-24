package org.elis.movieexplorer.controller;

import java.util.List;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.sala.request.EditSalaDTO;
import org.elis.movieexplorer.dto.sala.request.InsertSalaDTO;
import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.model.enums.Tipo;
import org.elis.movieexplorer.service.definition.SalaService;
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
public class SalaController {
	private final SalaService salaService;

//	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
//	@Operation(
//	    summary = "Inserimento sala",
//	    description = "Lo staff inserisce una nuova sala che viene creata e aggiunta al DB",
//	    responses = {
//	        @ApiResponse(
//	        	responseCode = "200",
//	        	description = "Sala creata",
//	        	content = @Content(
//	        		mediaType = MediaType.APPLICATION_JSON_VALUE,
//	        		schema = @Schema(
//	        			implementation = ResponseSalaDTO.class,
//	        			description = "Sala appena creata"
//	        		)
//	        	)
//	        ),
//	        @ApiResponse(
//	            responseCode = "409",
//	            description = "Nome sala già esistente",
//	            content = @Content(
//	            	mediaType = MediaType.APPLICATION_JSON_VALUE,
//	            	schema = @Schema(
//	            		implementation = ResponseErroreDTO.class,
//	            		description = SwaggerTags.ERROR_TAG
//	            	)
//	            )
//	        )
//	    }
//	)
//	@BadRequestApiResponse
//	@PostMapping("/staff/sala")
//	public ResponseEntity<ResponseSalaDTO> insert(@RequestBody @Valid InsertSalaDTO dto){
//		return ResponseEntity.ok(salaService.insert(dto));
//	}
	
	
	
	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce tutte le sale",
		description = "L'utente può vedere tutte le sale nel DB",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sale trovate",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSalaDTO[].class,
						description = "Tutte le sale nel DB"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessuna sala trovata",
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
	@GetMapping("/sala")
	public ResponseEntity<List<ResponseSalaDTO>> findAll(){
		return ResponseEntity.ok(salaService.findAll());
	}
	
	
	
	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce una sala",
		description = "L'utente può vedere una sala cercandola tramite ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sala trovata",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSalaDTO.class,
						description = "Sala con quell'ID"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Nessuna sala trovata",
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
	@GetMapping("/sala/{id}")
	public ResponseEntity<ResponseSalaDTO> findById(
			@PathVariable @Parameter(
					name = "ID", 
					description = "ID della sala da cercare", 
					required = true, 
					allowEmptyValue = false) Long id){
		return ResponseEntity.ok(salaService.findById(id));
	}
	
	
	
	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce le sale di un tipo",
		description = "L'utente può vedere le sale cercandole tramite Enum tipo",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sale trovate",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSalaDTO[].class,
						description = "Sale di quel tipo"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessuna sala trovata",
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
	@GetMapping("/sala/tipo/{tipo}")
	public ResponseEntity<List<ResponseSalaDTO>> findByTipo(
			@PathVariable @Parameter(
					name = "Tipo", 
					description = "Tipo di sala (IMAX, TRED o NORMALE)", 
					required = true, 
					allowEmptyValue = false) Tipo tipo){
		return ResponseEntity.ok(salaService.findByTipo(tipo));
	}
	
	
	
	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce la sala dal nome",
		description = "L'utente può vedere la sala cercandola tramite nome",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sala trovata",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSalaDTO.class,
						description = "Sala con quel nome"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Nessuna sala trovata",
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
	@GetMapping("/sala/nome/{nome}")
	public ResponseEntity<ResponseSalaDTO> findByNome(
			@PathVariable @Parameter(
			        name = "Nome", 
			        description = "Nome della sala da cercare", 
			        required = true, 
			        allowEmptyValue = false) String nome){
		return ResponseEntity.ok(salaService.findByNome(nome));
	}
	
	
	
//	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
//    @Operation(
//        summary = "Modifica sala",
//        description = "Lo staff modifica una sala in base al suo ID",
//        responses = {
//        	@ApiResponse(
//        		responseCode = "200",
//        		description = "Sala modificata",
//        		content = @Content(
//        			mediaType = MediaType.APPLICATION_JSON_VALUE,
//        			schema = @Schema(
//        				implementation = ResponseSalaDTO.class,
//        				description = "Sala con nuove modifiche"
//        			)
//        		)
//        	),
//        	@ApiResponse(
//            	responseCode = "404",
//            	description = "Sala o spettacolo non trovati",
//            	content = @Content(
//            		mediaType = MediaType.APPLICATION_JSON_VALUE,
//            		schema = @Schema(
//            			implementation = ResponseErroreDTO.class,
//            			description = SwaggerTags.ERROR_TAG
//            		)
//            	)
//            ),
//        	@ApiResponse(
//        		responseCode = "409",
//                description = "Nome sala già esistente",
//                content = @Content(
//                	mediaType = MediaType.APPLICATION_JSON_VALUE,
//                	schema = @Schema(
//                		implementation = ResponseErroreDTO.class,
//                		description = SwaggerTags.ERROR_TAG
//                	)
//                )
//            )
//        }
//    )
//	@BadRequestApiResponse
//	@PatchMapping("/staff/sala/{id}")
//	public ResponseEntity<ResponseSalaDTO> editById(
//			@PathVariable @Parameter(
//					name = "ID",
//					description = "ID della sala da modificare",
//					required = true,
//					allowEmptyValue = false) Long id,
//			@RequestBody EditSalaDTO dto){
//		return ResponseEntity.ok(salaService.editById(id, dto));
//	}
	
	
	
//	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
//    @Operation(
//        summary = "Elimina sala",
//        description = "Lo staff elimina una sala in base al suo ID",
//        responses = {
//        	@ApiResponse(
//        		responseCode = "200",
//        		description = "Sala rimossa"
//        	),
//        	@ApiResponse(
//            	responseCode = "404",
//            	description = "Sala non trovata",
//            	content = @Content(
//            		mediaType = MediaType.APPLICATION_JSON_VALUE,
//            		schema = @Schema(
//            			implementation = ResponseErroreDTO.class,
//            			description = SwaggerTags.ERROR_TAG
//            		)
//            	)
//            )
//        }
//    )
//	@DeleteMapping("/staff/sala/{id}")
//	public ResponseEntity<Void> removeById(
//			@PathVariable @Parameter(
//					name = "ID",
//					description = "ID della sala da eliminare",
//					required = true,
//					allowEmptyValue = false) Long id){
//		salaService.removeById(id);
//		return ResponseEntity.ok().build();
//	}
}
