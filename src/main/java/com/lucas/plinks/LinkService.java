package com.lucas.plinks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final PlinksProperties plinksProperties;

    public LinkService(LinkRepository linkRepository, PlinksProperties plinksProperties) {
        this.linkRepository = linkRepository;
        this.plinksProperties = plinksProperties;
    }

    public String shortLink(LinkRequestDTO linkDTO) {
        String url = linkDTO.url();
        String slug = generateUniqueRandomSlug();

        LinkModel toSave = new LinkModel(null, slug, url, 0, new Timestamp(System.currentTimeMillis()));
        linkRepository.save(toSave);

        return plinksProperties.getDomain() + "/" + slug;
    }

    private String generateUniqueRandomSlug() {
        String randomSlug;
        do {
            randomSlug = SlugGenerator.generateRandomSlug();
        } while (linkRepository.existsBySlug(randomSlug));
        return randomSlug;
    }
}
