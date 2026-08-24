package org.elis.movieexplorer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.utente.request.InsertUtenteDTO;
import org.elis.movieexplorer.dto.utente.request.LoginRequestDTO;
import org.elis.movieexplorer.dto.utente.response.LoginResponseDTO;
import org.elis.movieexplorer.dto.resetPassword.request.EditPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.request.ResetPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.response.ResetPasswordResponse;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDataDTO;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.service.definition.UtenteService;
import org.elis.movieexplorer.utility.BadRequestApiResponse;
import org.elis.movieexplorer.utility.SwaggerTags;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    @Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
    @Operation(
        summary = "Login cliente",
        description = "Permette all'utente di effettuare il login tramite mail e password",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Accesso effettuato",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = LoginResponseDTO.class,
                        description = "Messaggio di successo con email e nome dell'utente"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Password o email errate",
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO request) {
        System.out.println("Prova");
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, utenteService.login(request)).build();
    }

    @Tag(name = SwaggerTags.GUEST_TAG, description = SwaggerTags.GUEST_TAG_DESC)
    @Operation(
        summary = "Registrazione cliente",
        description = "Permette ad un cliente di registrarsi",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cliente creato",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = ResponseUtenteDTO.class,
                        description = "Messaggio di conferma di avvenuta registratione"
                    )
                )
            )
        }
    )
    @BadRequestApiResponse
    @PostMapping("/registrazione")
    public ResponseEntity<ResponseUtenteDTO> effettuaRegistrazione(@RequestBody @Valid InsertUtenteDTO request) {
        return ResponseEntity.ok(utenteService.insertCliente(request));
    }

    @Tag(name = SwaggerTags.ADMIN_TAG, description = SwaggerTags.ADMIN_TAG_DESC)
    @Operation(
        summary = "Aggiunta di un membro dello staff",
        description = "Permette ad un admin di aggiungere un membro dello staff",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Utente staff creato",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = ResponseUtenteDTO.class,
                        description = "Messaggio di conferma di avvenuta registratione"
                    )
                )
            )
        }
    )
    @BadRequestApiResponse
    @PostMapping("/admin/aggiungi_staff")
    public ResponseEntity<ResponseUtenteDTO> aggiungiStaff(@RequestBody @Valid InsertUtenteDTO request) {
        return ResponseEntity.ok(utenteService.insertStaff(request));
    }

    @GetMapping("/admin/lista_staff")
    public ResponseEntity<List<ResponseUtenteDataDTO>> listaStaff() {
        return ResponseEntity.ok(utenteService.findAllStaff());
    }

    @DeleteMapping("/admin/staff/{id}")
    public ResponseEntity<ResponseUtenteDTO> eliminaStaff(@PathVariable Long id) {
        return ResponseEntity.ok(utenteService.removeStaffById(id));
    }

    @DeleteMapping("/admin/staff")
    public ResponseEntity<ResponseUtenteDTO> eliminaStaffByEmail(@RequestParam String email) {
        return ResponseEntity.ok(utenteService.removeStaffByEmail(email));
    }

    @Tag(name = SwaggerTags.CUSTOMER_TAG, description = SwaggerTags.CUSTOMER_TAG_DESC)
    @Operation(
        summary = "Modifica password cliente",
        description = "Permette ad un cliente di modificare la propria password",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Password modificata",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = ResponseUtenteDTO.class,
                        description = "Messaggio di conferma di modifica password"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Password o email errate",
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
    @PatchMapping("/edit_password")
    public ResponseEntity<ResetPasswordResponse> editPassword( @RequestBody @Valid EditPasswordRequest request) {
    	
        return ResponseEntity.ok(utenteService.editPassword(request));
    }
    
    @Tag(name = SwaggerTags.CUSTOMER_TAG, description = SwaggerTags.CUSTOMER_TAG_DESC)
    @Operation(
        summary = "Reset password cliente",
        description = "Permette ad un cliente di resettare la propria password",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Password Resettata",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = ResponseUtenteDTO.class,
                        description = "Messaggio di conferma di reset password"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Password o email errate",
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
    @PatchMapping("/reset_password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest dto) {
    	
        return ResponseEntity.ok(utenteService.resetPassword(dto));
    }
    
    @GetMapping("/reset_password/{email}")
    public ResponseEntity<String> invioResetPassword(@PathVariable @Valid String email){
    	
    	return ResponseEntity.ok(utenteService.invioResetPassword(email));
    }

}
