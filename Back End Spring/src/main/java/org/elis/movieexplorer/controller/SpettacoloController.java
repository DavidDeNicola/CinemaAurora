package org.elis.movieexplorer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.spettacolo.request.EditSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.request.InsertSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.response.ResponseSpettacoloDTO;
import org.elis.movieexplorer.service.definition.SpettacoloService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SpettacoloController {
	private final SpettacoloService spettacoloService;

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Inserimento spettacolo.",
		description = "Lo staff inserisce un nuovo film che viene creato e aggiunto al DB.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Spettacolo creato",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSpettacoloDTO.class,
						description = "Spettacolo appena creato"
					)
				)
			),
			@ApiResponse(
				responseCode = "409",
				description = "Sala già occupata",
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
	@PostMapping("/staff/spettacolo")
	public ResponseEntity<ResponseSpettacoloDTO> insert(@RequestBody @Valid InsertSpettacoloDTO dto) {
		return ResponseEntity.ok(spettacoloService.insert(dto));
	}

	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce tutti gli spettacoli.",
		description = "L'utente può vedere tutti gli spettacoli nel DB.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Spettacoli trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSpettacoloDTO[].class,
						description = "Tutti gli spettacoli nel DB"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Nessuno spettacolo trovato",
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
	@GetMapping("/spettacolo")
	public ResponseEntity<List<ResponseSpettacoloDTO>> findAll() {
		return ResponseEntity.ok(spettacoloService.findAll());
	}

	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce una lista di spettacoli",
		description = "L'utente può vedere una lista di spettacoli cercandoli tramite la data",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Spettacoli trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSpettacoloDTO[].class,
						description = "Spettacoli che si svolgeranno in quella data"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Nessuno spettacolo trovato",
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
	@GetMapping("/spettacolo/{data}")
	public ResponseEntity<List<ResponseSpettacoloDTO>> findByData(@PathVariable @Parameter(
		name = "Data",
		description = "Data di svolgimento di cui cercare gli spettacoli",
		required = true,
		allowEmptyValue = false) LocalDate data) {
		return ResponseEntity.ok(spettacoloService.findByData(data));
	}

	@Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
	@Operation(
		summary = "Restituisce una lista di spettacoli di un film indicato",
		description = "L'utente può vedere una lista di spettacoli di un determinato film cercato tramite ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Spettacoli trovati",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSpettacoloDTO[].class,
						description = "Spettacoli di un determinato film"
					)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "Spettacoli non trovati",
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
	@GetMapping("/spettacolo/film/{id}")
	public ResponseEntity<List<ResponseSpettacoloDTO>> findByIdFilm(
			@PathVariable @Parameter(
				name = "ID",
				description = "ID del film di cui cercare gli spettacoli",
				required = true,
				allowEmptyValue = false) Long id) {
		return ResponseEntity.ok(spettacoloService.findByIdFilm(id));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Modifica spettacolo",
		description = "Lo staff modifica uno spettacolo in base al suo ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Spettacolo modificato",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
						implementation = ResponseSpettacoloDTO.class,
						description = "Spettacolo con nuove modifiche"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Spettacolo non trovato",
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
				description = "Spettacolo in conflitto con un altro",
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
	@PatchMapping("/staff/spettacolo/{id}")
	public ResponseEntity<ResponseSpettacoloDTO> editById(
			@PathVariable @Parameter(
				name = "ID",
				description = "ID del film da rimuovere",
				required = true,
				allowEmptyValue = false) Long id,
			@RequestBody @Valid EditSpettacoloDTO dto) {
		return ResponseEntity.ok(spettacoloService.editById(id, dto));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
		summary = "Elimina spettacolo",
		description = "Lo staff elimina uno spettacolo in base al suo ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Film rimosso"
			),
			@ApiResponse(
				responseCode = "404",
				description = "Spettacolo non trovato",
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
	@DeleteMapping("/staff/spettacolo/{id}")
	public ResponseEntity<Void> removeById(
			@PathVariable @Parameter(
				name = "ID",
				description = "ID dello spettacolo da rimuovere",
				required = true,
				allowEmptyValue = false) Long id) {
		spettacoloService.removeById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/admin/spettacolo/fatturato")
	public ResponseEntity<Map<Long, java.math.BigDecimal>> getFatturatoSpettacoli() {
		return ResponseEntity.ok(spettacoloService.getFatturatoSpettacoli());
	}
}
