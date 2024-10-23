package ie.seaniestack.urlshortener.controllers;

import ie.seaniestack.urlshortener.services.URLService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/r")
public class URLRedirect {

    private final URLService urlService;

    public URLRedirect(URLService urlService) {
        this.urlService = urlService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{shortURL}")
    public ResponseEntity<Void> redirectURL(@PathVariable String shortURL) {
        return urlService.getOriginalURL(shortURL);
    }
}
