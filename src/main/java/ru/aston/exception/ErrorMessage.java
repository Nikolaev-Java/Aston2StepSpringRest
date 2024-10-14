package ru.aston.exception;

public enum ErrorMessage {
    NON_FOUND("%s with id %d not found");
    private final String message;

    ErrorMessage(String msg) {
        this.message = msg;
    }

    public String formatMsg(Object... args) {
        return message.formatted(args);
    }
}
