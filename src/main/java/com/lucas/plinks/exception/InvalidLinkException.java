package com.lucas.plinks.exception;

public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException() {
        super("Invalid link provided");
    }
}
