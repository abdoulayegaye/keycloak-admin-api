package com.archidemo.keycloak.exception;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Ressource Keycloak introuvable (404 renvoyé par Keycloak) */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        log.warn("Ressource Keycloak introuvable : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", "Ressource introuvable dans Keycloak", 404));
    }

    /** Accès refusé par Keycloak */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException e) {
        log.warn("Accès refusé Keycloak : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("FORBIDDEN", "Accès refusé", 403));
    }

    /** Erreur HTTP générique renvoyée par le client Keycloak (409, 400, etc.) */
    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientError(ClientErrorException e) {
        int status = e.getResponse().getStatus();
        String body = extractBody(e);
        log.warn("Erreur client Keycloak [{}] : {}", status, body);
        return ResponseEntity.status(status)
                .body(new ErrorResponse("KEYCLOAK_CLIENT_ERROR", body, status));
    }

    /** Toute autre exception Keycloak (500 côté Keycloak, etc.) */
    @ExceptionHandler(WebApplicationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakError(WebApplicationException e) {
        int status = e.getResponse().getStatus();
        log.error("Erreur Keycloak [{}] : {}", status, e.getMessage());
        return ResponseEntity.status(status)
                .body(new ErrorResponse("KEYCLOAK_ERROR", e.getMessage(), status));
    }

    /** Erreurs de validation Bean Validation (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", details, 400));
    }

    /** Argument illégal (ex: realm déjà existant) */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", e.getMessage(), 400));
    }

    /** Fallback général */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        log.error("Erreur inattendue : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "Une erreur interne s'est produite", 500));
    }

    private String extractBody(ClientErrorException e) {
        try {
            return e.getResponse().readEntity(String.class);
        } catch (Exception ex) {
            return e.getMessage();
        }
    }
}
