package com.secfix.observedrepo.domain.entity.exception;

public class DuplicateObservedRepoException extends RuntimeException {
    public DuplicateObservedRepoException(String errorMessage) {
        super(errorMessage);
    }
}
