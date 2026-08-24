package org.elis.movieexplorer.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.mail.MessagingException;

import org.elis.movieexplorer.dto.errore.ResponseErroreDTO;
import org.elis.movieexplorer.dto.errore.ResponseErroreValidationDTO;
import org.elis.movieexplorer.exception.definition.*;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.security.SignatureException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> baseErrorHandler(MEBaseException e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage(e.getMessage());
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(e.getStatus()).body(dto);
    }

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
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setPath(w.getDescription(false));
        dto.setMessage("Il JSON inviato è vuoto o malformato. Per favore, inserisci i dati necessari.");
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> registrationErrorHandler(MERegistrationErrorException e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setPath(w.getDescription(false));
        dto.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> malformedJwtExceptionHandler(MalformedJwtException e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage("Token incompleto");
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> signatureExceptionHandler(SignatureException e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage("Token manomesso");
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> expiredJwtExceptionHandler(ExpiredJwtException e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage("Token scaduto");
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> emailGenericExceptionHandler(MailException e, WebRequest w){
    	ResponseErroreDTO dto = new ResponseErroreDTO();
    	dto.setMessage("Errore invio Email");
    	dto.setPath(w.getDescription(false));
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErroreDTO> httpClientErrorUnauthorizedExceptionHandler(HttpClientErrorException.Unauthorized e, WebRequest w){
        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setMessage("Utente non autorizzato.");
        dto.setPath(w.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseErroreDTO> dataAccessExceptionHandler(
            DataAccessException e,
            WebRequest w) {

        ResponseErroreDTO dto = new ResponseErroreDTO();
        dto.setPath(w.getDescription(false));

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof DuplicateKeyException) {
            dto.setMessage("I dati inseriti risultano già presenti.");
        }
        else if (e instanceof DataIntegrityViolationException) {
            dto.setMessage("I dati inseriti non sono validi.");
        }
        else if (e instanceof EmptyResultDataAccessException) {
            dto.setMessage("Risorsa non trovata.");
            status = HttpStatus.NOT_FOUND;
        }
        else if (e instanceof PermissionDeniedDataAccessException) {
            dto.setMessage("Operazione non consentita.");
            status = HttpStatus.FORBIDDEN;
        }
        else if (e instanceof QueryTimeoutException) {
            dto.setMessage("Operazione non completata. Riprovare più tardi.");
            status = HttpStatus.REQUEST_TIMEOUT;
        }
        else {
            dto.setMessage("Errore durante l'elaborazione della richiesta.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(dto);
    }
}
