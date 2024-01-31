package com.example.demo.transaction.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<NotEnoughMoneyException> notEnoughMoney(String message, Object... args) {
        return () -> new NotEnoughMoneyException(message, args);
    }

    public static Supplier<NotEnoughMoneyException> notEnoughMoney(String message) {
        return () -> new NotEnoughMoneyException(message);
    }
}
