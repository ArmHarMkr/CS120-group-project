package aua.core.exceptions;

public class MalformedStringException extends RuntimeException {
    public MalformedStringException() {
        this("Invalid String format");
    }

    public MalformedStringException(String message) {
        super(message);
    }
}
