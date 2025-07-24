package com.lucas.plinks;

import jakarta.annotation.Nullable;

public record LinkResponseDTO(String shortenedUrl, @Nullable byte[] qrCode) {
}
