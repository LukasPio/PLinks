package com.lucas.plinks;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public String shortUrl(String url) {
        String slug = getRandomAndUniqueSlug(url);
        return Constants.BASE_URL + "/" + slug;
    }

    private String getRandomAndUniqueSlug(String url) {
        StringBuilder randomSlug = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        boolean exists = false;
        do {
            Random random = new Random();
            for (int i = 0; i < Constants.SLUG_MAX_SIZE; i++) {
                char randomChar = chars.charAt(random.nextInt(chars.length()));
                randomSlug.append(randomChar);
            }
            if (linkRepository.existsBySlug(randomSlug.toString())) exists = true;
        } while (exists);
        return  randomSlug.toString();
    }

    public String getUrlFromSlug(String slug) {
        Optional<Link> link = linkRepository.findBySlug(slug);
        return link.map(value -> value.url).orElse(null);
    }
}
