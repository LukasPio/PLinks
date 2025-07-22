package com.lucas.plinks.exception;

public class SlugNotFoundException extends RuntimeException {
    public  SlugNotFoundException() {
        super("Slug not found");
    }
}
