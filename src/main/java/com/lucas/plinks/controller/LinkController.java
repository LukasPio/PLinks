package com.lucas.plinks.controller;

import com.lucas.plinks.DTO.LinkRequestDTO;
import com.lucas.plinks.service.LinkService;
import com.lucas.plinks.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/short")
    public ResponseEntity<ApiResponse<String>> shortLink(@RequestBody LinkRequestDTO linkDTO) {
        long start = System.currentTimeMillis();
        String shortenedLink;

        shortenedLink = linkService.shortLink(linkDTO);

        long elapsed = System.currentTimeMillis() - start;

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.toString(),
                "link was successfully shortened",
                shortenedLink,
                elapsed + " ms"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/redirect")
    public ResponseEntity<ApiResponse<String>> redirectLink(@RequestBody LinkRequestDTO linkDTO) {
        long start = System.currentTimeMillis();
        String originalUrl = linkService.redirect(linkDTO);
        long elapsed = System.currentTimeMillis() - start;

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.toString(),
                "original URL was successfully retreived",
                originalUrl,
                elapsed + " ms"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
