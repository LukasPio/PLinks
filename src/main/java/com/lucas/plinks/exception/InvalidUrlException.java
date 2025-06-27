package com.lucas.plinks.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException() {
        super("Invalid URL");
    }
}
