package com.mikhalov.taskonaut.exception;

public class TelegramCallbackNotFoundException extends RuntimeException {
    public TelegramCallbackNotFoundException(String message) {
        super(message);
    }
}
