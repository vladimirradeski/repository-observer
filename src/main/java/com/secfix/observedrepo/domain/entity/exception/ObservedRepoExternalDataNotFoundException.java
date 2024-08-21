package com.secfix.observedrepo.domain.entity.exception;

public class ObservedRepoExternalDataNotFoundException extends RuntimeException {
    public ObservedRepoExternalDataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
