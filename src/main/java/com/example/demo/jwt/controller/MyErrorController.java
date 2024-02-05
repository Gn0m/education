package com.example.demo.jwt.controller;

import com.example.demo.json_viewer.exceptions.Error;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Error> handler(HttpServletRequest request) {
        int status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return new ResponseEntity<>(new Error(status, "Token expired. " + e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }
}
