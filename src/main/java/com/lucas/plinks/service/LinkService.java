package com.lucas.plinks.service;

import com.lucas.plinks.DTO.CustomLinkRequestDTO;
import com.lucas.plinks.DTO.LinkRequestDTO;
import com.lucas.plinks.exception.InvalidUrlException;
import com.lucas.plinks.exception.SlugAlreadyRegisteredException;
import com.lucas.plinks.utils.PlinksProperties;
import com.lucas.plinks.utils.SlugGenerator;
import com.lucas.plinks.domain.LinkModel;
import com.lucas.plinks.repository.LinkRepository;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Optional;

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

    public String redirect(LinkRequestDTO linkDTO) {
        String shortenedUrl = linkDTO.url();
        if (shortenedUrl.endsWith("/")) shortenedUrl = shortenedUrl.substring(0, shortenedUrl.length() - 1);
        String slug = shortenedUrl.substring(shortenedUrl.lastIndexOf("/") + 1);

        Optional<LinkModel> originalLink = linkRepository.findBySlug(slug);

        if (originalLink.isEmpty()) throw new InvalidUrlException();

        LinkModel link = originalLink.get();
        link.setClicks(link.getClicks() + 1);
        linkRepository.save(link);

        return link.getOriginalUrl();
    }

    public String shortLinkWithCustomSlug(CustomLinkRequestDTO linkDTO) {
        String slug = linkDTO.slug();
        Optional<LinkModel> originalLink = linkRepository.findBySlug(slug);
        if (originalLink.isPresent()) throw new SlugAlreadyRegisteredException();
        LinkModel link = new LinkModel(null, slug, linkDTO.url(), 0, new Timestamp(System.currentTimeMillis()));
        linkRepository.save(link);

        return plinksProperties.getDomain() + "/" + slug;
    }
}
