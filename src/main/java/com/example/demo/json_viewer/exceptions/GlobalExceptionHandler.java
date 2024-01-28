package com.example.demo.json_viewer.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(NotFoundUserException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
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
}
