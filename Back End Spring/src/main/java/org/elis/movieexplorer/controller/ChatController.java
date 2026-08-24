package org.elis.movieexplorer.controller;

import java.util.List;

import org.elis.movieexplorer.dto.biglietto.response.ResponseBigliettoDTO;
import org.elis.movieexplorer.dto.chat.request.InsertChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseInfoChatDTO;
import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.message.request.InsertMessaggioDTO;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.service.definition.ChatService;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	@Tag(name = SwaggerTags.CUSTOMER_TAG, description = SwaggerTags.CUSTOMER_TAG_DESC)
	@Operation(
			summary = "Creazione nuova chat",
			description = "Il cliente può creare una nuova chat tra lui e lo staff coon oggetto e invio del primo messaggio",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Chat creata e inviato il primo messaggio"
							)
			}
			)
	@PostMapping("/cliente/chat")
	public ResponseEntity<Void> creaChat(@RequestBody @Valid InsertChatDTO dto, Authentication authenticator) {
		Utente u=(Utente) authenticator.getPrincipal();
		chatService.insertChat(dto, u);
		return ResponseEntity.ok().build();

	}

	@Tag(name = SwaggerTags.LOGGATO_TAG, description = SwaggerTags.LOGGATO_TAG_DESC)
	@Operation(
			summary = "Invio di un messaggio",
			description = "Invio da parte di chiunque (loggato) di un nuovo messaggio in una chat già esistente",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Messaggio inviato con successo"
							)
			}
			)


	@PostMapping("/user/inviamessaggio")
	public ResponseEntity<Void> inviaMessaggio(@RequestBody @Valid InsertMessaggioDTO dto, Authentication authenticator) {
		Utente u=(Utente) authenticator.getPrincipal();
		chatService.insertMessage(dto, u);
		return ResponseEntity.ok().build();
	}




	@Tag(name = SwaggerTags.LOGGATO_TAG, description = SwaggerTags.LOGGATO_TAG_DESC)
	@Operation(
			summary = "Recupero di tutte le chat",
			description = "Recupero da parte dell'utente della lista delle chat con annesso ultimo messaggio. Cliente recupera le proprie chat, Staff recupera tutte le chat ",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Chat recuperate",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = ResponseInfoChatDTO.class,
											description = "chat dell'utente"
											)

									)
							)
			}
			)




	@GetMapping("/user/chats")
	public ResponseEntity<List<ResponseInfoChatDTO>> listaChat(Authentication authenticator) {
		Utente u=(Utente) authenticator.getPrincipal();

		return ResponseEntity.ok(chatService.findAllChats(u));

	}
	@Tag(name = SwaggerTags.LOGGATO_TAG, description = SwaggerTags.LOGGATO_TAG_DESC)
	@Operation(
			summary = "Recupero di una singola chat",
			description = "Recupero da parte dell'utente di una singola chat.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Chat recuperate",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = ResponseInfoChatDTO.class,
											description = "chat dell'utente"
											)

									)
							),
					@ApiResponse(
							responseCode = "404",
							description = "Chat non trovata/messaggio non trovato",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = ResponseErroreDTO.class,
											description = SwaggerTags.ERROR_TAG
											)
									)
							),
					@ApiResponse(
							responseCode = "401",
							description = "Non hai accesso a questo contenuto",
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






	//singola chat
	@GetMapping("/user/chat")
	public ResponseEntity<ResponseChatDTO> singolaChat(@RequestParam Long id, Authentication authenticator) {
		Utente u=(Utente) authenticator.getPrincipal();

		return ResponseEntity.ok(chatService.findChatById(id, u));
	}

	@Tag(name = SwaggerTags.STAFF_TAG, description = SwaggerTags.STAFF_TAG_DESC)
	@Operation(
			summary = "Aggiorna lo stato di una chat",
			description = "Cambia lo stato del ticket tra in_attesa , aperto o chiuso",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Chat recuperate",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = ResponseInfoChatDTO.class,
											description = "chat dell'utente"
											)

									)
							),
					@ApiResponse(
							responseCode = "404",
							description = "Chat non trovata",
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

	@PatchMapping("/staff/cambiostato")
	public ResponseEntity<Void> cambioStato(@RequestParam Long idChat) {
		chatService.cambiaStatoChat(idChat);
		return ResponseEntity.ok().build();
	}


	@Tag(name = SwaggerTags.LOGGATO_TAG, description = SwaggerTags.LOGGATO_TAG_DESC)
	@Operation(
			summary = "Conteggio delle chat non lette",
			description = "conteggio di quante chat con messaggi non letti possiede l'utente",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "conteggio effettuato"


							)

			}
			)

	@GetMapping("/user/contatore")
	public ResponseEntity<Integer> contatoreChatNonLette(Authentication authenticator){
		Utente u=(Utente) authenticator.getPrincipal();
		return ResponseEntity.ok(chatService.countChatNotRead(u));

	}



}
