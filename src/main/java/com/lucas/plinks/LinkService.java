package com.lucas.plinks;

import com.lucas.plinks.exception.InvalidLinkException;
import com.lucas.plinks.exception.LinkAlreadyExpiresException;
import com.lucas.plinks.exception.SlugAlreadyExistsException;
import com.lucas.plinks.exception.SlugNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public String shortUrl(LinkRequestDTO requestDTO) {
        if (!requestDTO.url().startsWith("https://") || isInvalidLink(requestDTO.url()))
            throw new InvalidLinkException();

        String slug;

        if (requestDTO.slug() == null) {
            slug = getRandomAndUniqueSlug();
        } else {
            slug = requestDTO.slug().replace(" ", "");
            if (linkRepository.existsBySlug(slug)) throw new SlugAlreadyExistsException();
        }

        LocalDateTime expiresAt = requestDTO.expiresAfter() != null ? LocalDateTime.now().plusSeconds(requestDTO.expiresAfter()) : null;

        Link link = new Link(null, requestDTO.url(), slug, expiresAt,0);

        linkRepository.save(link);

        return Constants.BASE_URL + "/" + slug;
    }

    private String getRandomAndUniqueSlug() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String slug;

        do {
            StringBuilder randomSlug = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < Constants.SLUG_MAX_SIZE; i++) {
                char randomChar = chars.charAt(random.nextInt(chars.length()));
                randomSlug.append(randomChar);
            }
            slug = randomSlug.toString();
        } while (linkRepository.existsBySlug(slug));
        return slug;
    }

    public String redirect(String slug) {
        Optional<Link> optionalLink = linkRepository.findBySlug(slug);

        if (optionalLink.isEmpty())
            throw new SlugNotFoundException();

        Link link = optionalLink.get();

        if (link.expiresAt != null && link.expiresAt.isBefore(LocalDateTime.now()))
            throw new LinkAlreadyExpiresException(link.expiresAt);

        return link.url;
    }

    private boolean isInvalidLink(String link) {
        try {
            new URL(link);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public int getClicksFromShortenedLink(SlugRequestDTO slug) {
        Optional<Link> link = linkRepository.findBySlug(slug.slug().replace(" ", ""));
        if (link.isEmpty()) throw new SlugNotFoundException();
        return link.get().clicks;
    }
}
