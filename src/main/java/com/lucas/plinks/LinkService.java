package com.lucas.plinks;

import com.lucas.plinks.exception.InvalidLinkException;
import com.lucas.plinks.exception.SlugAlreadyExistsException;
import com.lucas.plinks.exception.SlugNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
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
        if (requestDTO.slug() != null) {
            slug = requestDTO.slug().replace(" ", "");
            if (linkRepository.existsBySlug(slug)) throw new SlugAlreadyExistsException();
        } else {
            slug = getRandomAndUniqueSlug();
        }

        Link link = new Link(null, requestDTO.url(), slug, 0);

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
        Optional<Link> link = linkRepository.findBySlug(slug);
        if (link.isPresent()) {
            Link toUpdate = link.get();
            toUpdate.clicks = toUpdate.clicks + 1;
            linkRepository.save(toUpdate);
        }
        return link.map(value -> value.url).orElse(null);
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
