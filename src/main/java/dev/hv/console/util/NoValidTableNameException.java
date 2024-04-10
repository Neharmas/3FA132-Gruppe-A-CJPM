package dev.hv.console.util;

public class NoValidTableNameException extends Exception {
    public NoValidTableNameException(String errorMessage) {
        super(errorMessage);
    }
}
