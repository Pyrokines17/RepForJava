package ru.nsu.gunko.factory.exceptions;

public class BadCreatingException extends RuntimeException {
    public BadCreatingException(String message) {
        super(message);
    }

    public BadCreatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
