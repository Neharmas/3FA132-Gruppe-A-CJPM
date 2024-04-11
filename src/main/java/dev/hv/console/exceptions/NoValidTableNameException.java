package dev.hv.console.exceptions;

public class NoValidTableNameException extends Exception {
    public NoValidTableNameException(String errorMessage) {
        super(errorMessage);
    }
}
