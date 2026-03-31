package com.archidemo.keycloak.exception;

import feign.FeignException;
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

    /** 404 renvoyé par Keycloak via Feign */
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFound(FeignException.NotFound e) {
        log.warn("Ressource Keycloak introuvable : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", "Ressource introuvable dans Keycloak", 404));
    }

    /** 403 renvoyé par Keycloak via Feign */
    @ExceptionHandler(FeignException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbidden(FeignException.Forbidden e) {
        log.warn("Accès refusé Keycloak : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("FORBIDDEN", "Accès refusé", 403));
    }

    /** 409 / 400 et autres erreurs client renvoyées par Keycloak via Feign */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignError(FeignException e) {
        int status = e.status();
        String message = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
        log.warn("Erreur Keycloak [{}] : {}", status, message);
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null) httpStatus = HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse("KEYCLOAK_ERROR", message, status));
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

    /** Argument illégal (ex: Location header absent) */
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
}
