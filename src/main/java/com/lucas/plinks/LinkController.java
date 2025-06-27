package com.lucas.plinks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/short")
    public ResponseEntity<HashMap<String, String>> shortLink(@RequestBody LinkRequestDTO linkDTO) {
        long start = System.currentTimeMillis();
        String shortenedLink = linkService.shortLink(linkDTO);

        String elapsedTime = System.currentTimeMillis() - start + " ms";

        HashMap<String, String> response = new HashMap<>();
        response.put("status", "200");
        response.put("shortenedLink", shortenedLink);
        response.put("elapsedTime", elapsedTime);

        return ResponseEntity.ok(response);
    }
}
