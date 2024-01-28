package com.example.demo.json_viewer.exceptions;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException(String message) {
        super(message);
    }

    public NotFoundUserException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<NotFoundUserException> notFoundUser(String message, Object... args) {
        return () -> new NotFoundUserException(message, args);
    }

    public static Supplier<NotFoundUserException> notFoundUser(String message) {
        return () -> new NotFoundUserException(message);
    }
}
