package com.lucas.plinks.utils;

import java.security.SecureRandom;

public class SlugGenerator {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SLUG_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomSlug() {
        StringBuilder stringBuilder = new StringBuilder(SLUG_LENGTH);
        for (int i = 0; i < SLUG_LENGTH; i++) {
            int index = random.nextInt(BASE62.length());
            stringBuilder.append(BASE62.charAt(index));
        }
        return  stringBuilder.toString();
    }
}
