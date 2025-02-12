package com.showcaseScore.movieApp.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.TransactionalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handle EntityNotFoundException (e.g., movie not found by IMDb ID)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Handle MoviesNotFoundException (e.g., no movies found for a genre)
    @ExceptionHandler(MoviesNotFoundException.class)
    public ResponseEntity<String> handleMoviesNotFoundException(MoviesNotFoundException e) {
        log.error("No movies found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Handle IllegalArgumentException (e.g., invalid input)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid input: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Handle ApiCallException (e.g., failure in OMDB or TMDB API calls)
    @ExceptionHandler(ApiCallException.class)
    public ResponseEntity<String> handleApiCallException(ApiCallException e) {
        log.error("API call failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    // Handle DataAccessException (e.g., database connection issues)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        log.error("Database access error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database access error: " + e.getMessage());
    }

    // Handle TransactionalException (e.g., transactional operation failure)
    @ExceptionHandler(TransactionalException.class)
    public ResponseEntity<String> handleTransactionalException(TransactionalException e) {
        log.error("Transactional error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transactional error: " + e.getMessage());
    }

    // Handle all other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}