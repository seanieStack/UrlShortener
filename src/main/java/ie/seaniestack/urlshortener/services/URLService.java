package ie.seaniestack.urlshortener.services;

import ie.seaniestack.urlshortener.UrlShortenerConstants;
import ie.seaniestack.urlshortener.entities.UrlLink;
import ie.seaniestack.urlshortener.repositories.URLRepository;
import ie.seaniestack.urlshortener.util.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class URLService {

    private final URLRepository urlRepository;

    private static final Logger logger = LoggerFactory.getLogger(URLService.class);
    private final String HTTPS_PREFIX = "https://";
    private final String HTTP_PREFIX = "http://";

    public URLService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public ResponseEntity<Void> getOriginalURL(String shortURL) {
        Optional<UrlLink> url = urlRepository.findByShortURL(shortURL);

        if (url.isPresent()) {
            String originalURL = url.get().getOriginalURL();

            if (!originalURL.startsWith(HTTP_PREFIX) && !originalURL.startsWith(HTTPS_PREFIX)) {
                originalURL = HTTPS_PREFIX + originalURL;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalURL));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Map<String, String>> createShortURL(String originalURL) {


        if (!originalURL.startsWith(HTTP_PREFIX) && !originalURL.startsWith(HTTPS_PREFIX)) {
            originalURL = HTTPS_PREFIX + originalURL;
        }

        Optional<UrlLink> url = urlRepository.findByOriginalURL(originalURL);
        Map<String, String> response = new HashMap<>();

        if(!URLUtils.isValidURL(originalURL)) {
            response.put("error", "Invalid URL");
            logger.error("Invalid URL: {}", originalURL);
            return ResponseEntity.badRequest().body(response);
        }

        if (url.isPresent()) {
            String shortUrl = UrlShortenerConstants.BASE_URL + "r/" + url.get().getShortURL();

            response.put("shortUrl", shortUrl);
        }
        else {
            String shortURL = URLUtils.generateShortURL(urlRepository);
            UrlLink newURL = new UrlLink(originalURL, shortURL);

            String shortUrl = UrlShortenerConstants.BASE_URL + "r/" + urlRepository.save(newURL).getShortURL();

            response.put("shortUrl", shortUrl);
        }

        return ResponseEntity.ok(response);
    }
}
