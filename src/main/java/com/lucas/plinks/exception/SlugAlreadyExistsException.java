package com.lucas.plinks.exception;

public class SlugAlreadyExistsException extends RuntimeException{
    public SlugAlreadyExistsException() {
        super("Slug already exists");
    }
}
