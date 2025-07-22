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
