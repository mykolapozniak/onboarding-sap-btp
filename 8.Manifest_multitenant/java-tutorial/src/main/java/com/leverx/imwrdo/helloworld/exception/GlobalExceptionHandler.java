package com.leverx.imwrdo.helloworld.exception;

import com.leverx.imwrdo.helloworld.dto.ErrorResponseDTO;
import com.sap.xsa.core.instancemanager.client.ImClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * This class handles various exceptions and returns appropriate HTTP responses.
 */
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler for generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return createErrorResponse(
                "An unexpected error occurred",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotAuthorizedException(Exception ex) {
        return createErrorResponse(ex.getMessage(), "Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(Exception ex) {
        return createErrorResponse(ex.getMessage(), "Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseDTO> handleSQLException(Exception ex) {
        return createErrorResponse(
                "Please check provided data",
                "Bad request",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImClientException.class)
    public ResponseEntity<ErrorResponseDTO> handleImClientException(Exception ex) {
        return createErrorResponse(
                ex.getMessage(),
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Helper method for creating error responses
    private ResponseEntity<ErrorResponseDTO> createErrorResponse(
            String message, String error, HttpStatus status) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(error,
                        message,
                        status.value(),
                        LocalDateTime.now()),
                status);
    }

}
