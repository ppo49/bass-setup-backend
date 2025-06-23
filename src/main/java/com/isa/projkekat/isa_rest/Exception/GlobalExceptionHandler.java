package com.isa.projkekat.isa_rest.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Hvata izuzetke validacije (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // Vraca 400 Bad Request
    }

    // Hvata ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Vraca status kod i poruku direktno iz bačenog ResponseStatusException
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Operacija nije uspela zbog kršenja integriteta podataka. Detalji: " + ex.getRootCause().getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT); // Vraca 409 Conflict
    }


    // Hvata sve ostale neuhvacene izuzetke
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Loguj pun stack trace za debug
        return new ResponseEntity<>("Došlo je do interne serverske greške: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Vraca 500 Internal Server Error
    }
}