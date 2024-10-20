package ie.seaniestack.urlshortener.controllers;

import ie.seaniestack.urlshortener.services.URLService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class URLManagement {

    private final URLService urlService;

    public URLManagement(URLService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/create")
    public String createURL(@RequestBody String originalURL) {
        return urlService.createShortURL(originalURL);
    }
}
