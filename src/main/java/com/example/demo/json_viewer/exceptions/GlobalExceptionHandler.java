package com.example.demo.json_viewer.exceptions;

import com.example.demo.transaction.exception.NotEnoughMoneyException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<Error> catchResourceNotFoundException(NotFoundUserException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> catchResourceNotFoundException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> list = new ArrayList<>();
        fieldErrors.forEach(fieldError ->
                list.add(fieldError.getField())
        );
        String errorStr = "Not valid values: " + String.join(",", list);
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), errorStr), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<Error> catchResourceNotFoundException(NotEnoughMoneyException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlredyExistsException.class)
    public ResponseEntity<Error> catchResourceNotFoundException(UserAlredyExistsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<Error> handleAuthenticationException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), "Username or password is incorrect. " + e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<Error> handleAccountStatusException(AccountStatusException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), "User account is abnormal. " + e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Error> handleInvalidBearerTokenException(ExpiredJwtException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), "The access token provided is expired. " + e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.FORBIDDEN.value(), "No permission. " + e.getMessage()),
                HttpStatus.FORBIDDEN);
    }


}
