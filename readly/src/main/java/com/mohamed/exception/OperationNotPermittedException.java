package com.mohamed.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String string) {
        super(string);
    }
}
