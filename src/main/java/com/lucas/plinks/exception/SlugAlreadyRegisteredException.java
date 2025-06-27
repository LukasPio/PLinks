package com.lucas.plinks.exception;

public class SlugAlreadyRegisteredException extends RuntimeException{
    public SlugAlreadyRegisteredException() {
        super("Slug already registered");
    }
}
