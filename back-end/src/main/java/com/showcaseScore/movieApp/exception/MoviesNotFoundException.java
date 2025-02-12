package com.showcaseScore.movieApp.exception;

public class MoviesNotFoundException extends RuntimeException {
    public MoviesNotFoundException(String message) {
        super(message);
    }
}
