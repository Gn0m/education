package com.example.demo.json_viewer.exceptions;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class UserAlredyExistsException extends RuntimeException {

    public UserAlredyExistsException(String message) {
        super(message);
    }

    public UserAlredyExistsException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<NotFoundUserException> alredyExists(String message, Object... args) {
        return () -> new NotFoundUserException(message, args);
    }

    public static Supplier<NotFoundUserException> alredyExists(String message) {
        return () -> new NotFoundUserException(message);
    }
}

