package com.lucas.plinks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<ApiResponse<LinkResponseDTO>> shortUrl(@RequestBody LinkRequestDTO requestDTO) throws IOException {
        LinkResponseDTO shortenedLink = linkService.shortUrl(requestDTO);
        ApiResponse<LinkResponseDTO> response = new ApiResponse<>("Link shortened successfully", new Timestamp(
                System.currentTimeMillis()),
                HttpStatus.OK.value(),
                shortenedLink);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<String>> redirect(@PathVariable String slug) throws URISyntaxException {
        String url = linkService.redirect(slug);
        if (url == null) {
            ApiResponse<String> response = new ApiResponse<>(
                    "Slug was not found",
                    new Timestamp(System.currentTimeMillis()),
                    404,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(url)).build();
    }

    @PostMapping("/clicks")
    public ResponseEntity<ApiResponse<Integer>> clicks(@RequestBody SlugRequestDTO slug) {
        int clicks = linkService.getClicksFromShortenedLink(slug);
        ApiResponse<Integer> response = new ApiResponse<>(
                "Successfully got clicks from link",
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.OK.value(), clicks);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
