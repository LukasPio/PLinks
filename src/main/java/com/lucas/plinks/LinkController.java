package com.lucas.plinks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/short/{url}")
    public ResponseEntity<String> shortUrl(@PathVariable String url) {
        String shortenedLink = linkService.shortUrl(url);
        return ResponseEntity.status(HttpStatus.OK).body(shortenedLink);
    }

    @GetMapping("/{slug}")
    public String redirect(@PathVariable String slug) {
        String url = linkService.getUrlFromSlug(slug);
        if (url == null) return "Not found";
        return "redirect:" + url;
    }
}
