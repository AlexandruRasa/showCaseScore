package com.showcaseScore.movieApp.exception;

public class ApiCallException extends RuntimeException {
    public ApiCallException(String message) {
        super(message);
    }
}