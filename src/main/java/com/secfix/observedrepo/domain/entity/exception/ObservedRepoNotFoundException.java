package com.secfix.observedrepo.domain.entity.exception;

public class ObservedRepoNotFoundException extends RuntimeException {
    public ObservedRepoNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
