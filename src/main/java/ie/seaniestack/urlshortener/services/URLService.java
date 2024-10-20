package ie.seaniestack.urlshortener.services;

import ie.seaniestack.urlshortener.entities.URL;
import ie.seaniestack.urlshortener.repositories.URLRepository;
import ie.seaniestack.urlshortener.util.URLUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.util.Optional;

@Service
public class URLService {

    private final URLRepository urlRepository;

    public URLService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public ResponseEntity<Void> getOriginalURL(String shortURL) {
        Optional<URL> url = urlRepository.findByShortURL(shortURL);

        if (url.isPresent()) {
            String originalURL = url.get().getOriginalURL();

            // Ensure the original URL has a protocol
            if (!originalURL.startsWith("http://") && !originalURL.startsWith("https://")) {
                originalURL = "https://" + originalURL;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalURL));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public String createShortURL(String originalURL) {
        Optional<URL> url = urlRepository.findByOriginalURL(originalURL);

        if (url.isPresent()) {
            return "localhost:8080/r/" + url.get().getShortURL();
        }

        String shortURL = URLUtils.generateShortURL(urlRepository);
        URL newURL = new URL(originalURL, shortURL);
        return "localhost:8080/r/" + urlRepository.save(newURL).getShortURL();
    }

}
