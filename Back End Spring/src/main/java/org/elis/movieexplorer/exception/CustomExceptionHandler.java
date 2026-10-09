package org.elis.movieexplorer.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.errore.ResponseErroreValidationDTO;
import org.elis.movieexplorer.exception.definition.MEBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    // ==========================================
    // ECCEZIONI CUSTOM (tutte le sottoclassi di MEBaseException)
    // ==========================================

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> baseErrorHandler(MEBaseException e, WebRequest w){
        return risposta(e.getStatus(), e.getMessage(), w);
    }

    // ==========================================
    // DATI DELLA RICHIESTA NON VALIDI
    // ==========================================

    @ExceptionHandler
    public ResponseEntity<ResponseErroreValidationDTO> validationHandler(MethodArgumentNotValidException e, WebRequest w){
        ResponseErroreValidationDTO dto = new ResponseErroreValidationDTO();
        dto.setPath(w.getDescription(false));
        dto.setMessage("Alcuni dei campi inviati non sono corretti");
        Map<String, String> errori = e.getFieldErrors().stream()
                .collect(Collectors.toMap(t->t.getField(), t->t.getDefaultMessage()));
        dto.setErrori(errori);
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> missingBodyHandler(HttpMessageNotReadableException e, WebRequest w){
        return risposta(HttpStatus.BAD_REQUEST,
                "Il JSON inviato è vuoto o malformato. Per favore, inserisci i dati necessari.", w);
    }

    // es. GET /staff/film/abc quando l'id deve essere un numero
    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> typeMismatchHandler(TypeMismatchException e, WebRequest w){
        return risposta(HttpStatus.BAD_REQUEST, "Uno dei parametri inviati ha un formato non valido.", w);
    }

    // ==========================================
    // AUTENTICAZIONE (arrivano dall'AuthFilter tramite HandlerExceptionResolver)
    // ==========================================

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> malformedJwtExceptionHandler(MalformedJwtException e, WebRequest w){
        return risposta(HttpStatus.UNAUTHORIZED, "Token incompleto", w);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> signatureExceptionHandler(SignatureException e, WebRequest w){
        return risposta(HttpStatus.UNAUTHORIZED, "Token manomesso", w);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> expiredJwtExceptionHandler(ExpiredJwtException e, WebRequest w){
        return risposta(HttpStatus.UNAUTHORIZED, "Token scaduto", w);
    }

    // qualsiasi altro problema del token o utente del token non più esistente
    @ExceptionHandler({ JwtException.class, AuthenticationException.class })
    public ResponseEntity<ResponseErroreDTO> tokenNonValidoHandler(Exception e, WebRequest w){
        return risposta(HttpStatus.UNAUTHORIZED, "Sessione scaduta o non valida: effettua di nuovo il login", w);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> accessDeniedHandler(AccessDeniedException e, WebRequest w){
        return risposta(HttpStatus.FORBIDDEN, "Non hai i permessi per questa operazione.", w);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> httpClientErrorUnauthorizedExceptionHandler(HttpClientErrorException.Unauthorized e, WebRequest w){
        return risposta(HttpStatus.UNAUTHORIZED, "Utente non autorizzato.", w);
    }

    // ==========================================
    // SERVIZI ESTERNI
    // ==========================================

    // il server di posta non risponde o non è configurato: non è un problema dell'utente
    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> emailGenericExceptionHandler(MailException e, WebRequest w){
        log.error("Errore durante l'invio dell'email", e);
        return risposta(HttpStatus.SERVICE_UNAVAILABLE, "Errore invio Email", w);
    }

    // ==========================================
    // DATABASE
    // ==========================================

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseErroreDTO> dataAccessExceptionHandler(
            DataAccessException e,
            WebRequest w) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String messaggio;
        if (e instanceof DuplicateKeyException) {
            messaggio = "I dati inseriti risultano già presenti.";
        }
        else if (e instanceof DataIntegrityViolationException) {
            messaggio = "I dati inseriti non sono validi.";
        }
        else if (e instanceof EmptyResultDataAccessException) {
            messaggio = "Risorsa non trovata.";
            status = HttpStatus.NOT_FOUND;
        }
        else if (e instanceof PermissionDeniedDataAccessException) {
            messaggio = "Operazione non consentita.";
            status = HttpStatus.FORBIDDEN;
        }
        else if (e instanceof QueryTimeoutException) {
            messaggio = "Operazione non completata. Riprovare più tardi.";
            status = HttpStatus.REQUEST_TIMEOUT;
        }
        else {
            log.error("Errore di accesso al database", e);
            messaggio = "Errore durante l'elaborazione della richiesta.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return risposta(status, messaggio, w);
    }

    // ==========================================
    // RETE DI SICUREZZA: tutto ciò che non è gestito sopra
    // ==========================================

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> genericExceptionHandler(Exception e, WebRequest w){
        // le eccezioni standard di Spring (405, 404 su URL inesistente, ResponseStatusException...)
        // conoscono già il loro status: lo rispettiamo
        if (e instanceof ErrorResponse errorResponse) {
            String dettaglio = errorResponse.getBody().getDetail();
            return risposta(errorResponse.getStatusCode(), dettaglio != null ? dettaglio : e.getMessage(), w);
        }
        log.error("Errore non gestito", e);
        return risposta(HttpStatus.INTERNAL_SERVER_ERROR, "Si è verificato un errore imprevisto.", w);
    }

    // ==========================================
    // HELPER
    // ==========================================

    private ResponseEntity<ResponseErroreDTO> risposta(HttpStatusCode status, String messaggio, WebRequest w) {
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage(messaggio);
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(status).body(dto);
    }
}
