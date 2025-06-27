package com.lucas.plinks.service;

import com.lucas.plinks.DTO.LinkRequestDTO;
import com.lucas.plinks.exception.InvalidPasswordException;
import com.lucas.plinks.exception.InvalidUrlException;
import com.lucas.plinks.exception.SlugAlreadyRegisteredException;
import com.lucas.plinks.utils.PlinksProperties;
import com.lucas.plinks.utils.SlugGenerator;
import com.lucas.plinks.domain.LinkModel;
import com.lucas.plinks.repository.LinkRepository;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Objects;
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
        LinkModel link;
        if (linkDTO.slug() != null) link = shortLinkWithCustomSlug(linkDTO);
        else link = shortLinkWithRandomSlug(linkDTO.url());

        if (linkDTO.password() != null) link.setPassword(linkDTO.password());

        linkRepository.save(link);
        return plinksProperties.getDomain() + "/" + link.getSlug();
    }

    public String redirect(LinkRequestDTO linkDTO) {
        String shortenedUrl = linkDTO.url();
        if (shortenedUrl.endsWith("/")) shortenedUrl = shortenedUrl.substring(0, shortenedUrl.length() - 1);
        String slug = shortenedUrl.substring(shortenedUrl.lastIndexOf("/") + 1);

        Optional<LinkModel> originalLink = linkRepository.findBySlug(slug);

        if (originalLink.isEmpty()) throw new InvalidUrlException();

        LinkModel link = originalLink.get();

        if (!Objects.equals(link.getPassword(), linkDTO.password()) && link.getPassword() != null)
            throw new InvalidPasswordException();

        link.setClicks(link.getClicks() + 1);
        linkRepository.save(link);

        return link.getOriginalUrl();
    }

    private LinkModel shortLinkWithRandomSlug(String url) {
        String slug = generateUniqueRandomSlug();

        return new LinkModel(null, slug, url, 0, new Timestamp(System.currentTimeMillis()), null);
    }

    private String generateUniqueRandomSlug() {
        String randomSlug;
        do {
            randomSlug = SlugGenerator.generateRandomSlug();
        } while (linkRepository.existsBySlug(randomSlug));
        return randomSlug;
    }

    private LinkModel shortLinkWithCustomSlug(LinkRequestDTO linkDTO) {
        String slug = linkDTO.slug();
        Optional<LinkModel> originalLink = linkRepository.findBySlug(slug);
        if (originalLink.isPresent()) throw new SlugAlreadyRegisteredException();

        return new LinkModel(null, slug, linkDTO.url(), 0, new Timestamp(System.currentTimeMillis()), null);
    }
}
