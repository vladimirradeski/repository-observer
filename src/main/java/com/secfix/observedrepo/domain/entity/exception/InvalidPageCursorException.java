package com.secfix.observedrepo.domain.entity.exception;

public class InvalidPageCursorException extends RuntimeException {
    public InvalidPageCursorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
