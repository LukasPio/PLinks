package com.lucas.plinks;

import jakarta.annotation.Nullable;

public record LinkRequestDTO(String url, @Nullable String slug, @Nullable Integer expiresAfter) {
}
