package ru.nsu.gunko.factory.exceptions;

public class NotEnoughParamException extends RuntimeException {
    public NotEnoughParamException(String message) {
        super(message);
    }
}
