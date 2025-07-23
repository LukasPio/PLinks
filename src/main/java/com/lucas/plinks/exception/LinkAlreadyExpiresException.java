package com.lucas.plinks.exception;

import java.time.LocalDateTime;

public class LinkAlreadyExpiresException extends RuntimeException {
    public LocalDateTime expiredAt;
    public LinkAlreadyExpiresException(LocalDateTime expiredAt) {
        super("Link already expires");
        this.expiredAt = expiredAt;
    }
}
