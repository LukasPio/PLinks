package com.lucas.plinks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/short")
    public ResponseEntity<ApiResponse<String>> shortUrl(@RequestBody LinkRequestDTO requestDTO) {
        String shortenedLink = linkService.shortUrl(requestDTO);
        ApiResponse<String> response = new ApiResponse<>("Link shortened successfully", new Timestamp(
                System.currentTimeMillis()),
                HttpStatus.OK.value(),
                shortenedLink);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{slug}")
    public String redirect(@PathVariable String slug) {
        String url = linkService.getUrlFromSlug(slug);
        if (url == null) return "Not found";
        return "redirect:" + url;
    public ResponseEntity<Void> redirect(@PathVariable String slug) throws URISyntaxException {
        String url = linkService.redirect(slug);
        if (url == null) {
            URI notFound = new URI(Constants.BASE_URL + "/not-found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).location(notFound).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(url)).build();
    }
    }
}
