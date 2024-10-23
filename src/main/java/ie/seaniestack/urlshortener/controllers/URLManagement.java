package ie.seaniestack.urlshortener.controllers;

import ie.seaniestack.urlshortener.services.URLService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/manage")
public class URLManagement {

    private final URLService urlService;

    public URLManagement(URLService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> createURL(@RequestBody Map<String, String> urlRequest) {
        return urlService.createShortURL(urlRequest.get("url"));
    }
}
