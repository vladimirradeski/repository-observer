package com.secfix.observedrepo.api.exception;

import com.secfix.observedrepo.api.dto.ErrorResponse;
import com.secfix.observedrepo.domain.entity.exception.DuplicateObservedRepoException;
import com.secfix.observedrepo.domain.entity.exception.InvalidPageCursorException;
import com.secfix.observedrepo.domain.entity.exception.ObservedRepoNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateObservedRepoException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateObservedRepoException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPageCursorException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPageCursorException(InvalidPageCursorException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObservedRepoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ObservedRepoNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.NOT_FOUND), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(toErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse toErrorResponse(List<String> errors, HttpStatus status) {
        return ErrorResponse.builder()
                .errors(errors)
                .status(status.value())
                .build();
    }
}
