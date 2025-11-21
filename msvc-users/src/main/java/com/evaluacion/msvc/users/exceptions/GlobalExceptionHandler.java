package com.evaluacion.msvc.users.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Controla todas las excepciones a nivel global
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", message);
        return new ResponseEntity<>(body, status);
    }

    // Email duplicado u otra restricción única
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return error(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
    }

    // Validaciones @Valid fallidas
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldError().getDefaultMessage();
        return error(HttpStatus.BAD_REQUEST, mensaje);
    }

    // Errores personalizados
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Todo error no previsto
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor");
    }
}
