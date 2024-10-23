package ie.seaniestack.urlshortener.util;

import ie.seaniestack.urlshortener.UrlShortenerConstants;
import ie.seaniestack.urlshortener.entities.UrlLink;
import ie.seaniestack.urlshortener.repositories.URLRepository;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class URLUtils {

    private final Set<String> concurentURLSet = ConcurrentHashMap.newKeySet();
    private final SecureRandom random = new SecureRandom();

    public synchronized String generateShortURL(URLRepository urlRepository) {
        if (concurentURLSet.isEmpty()) {
            updateConcurrentURLSet(urlRepository);
        }

        String shortURL;
        do {
            shortURL = generateRandomString();
        } while (concurentURLSet.contains(shortURL));

        concurentURLSet.add(shortURL);
        return shortURL;
    }

    private String generateRandomString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < UrlShortenerConstants.URL_LENGTH; i++) {
            int character = random.nextInt(UrlShortenerConstants.ALPHA_NUMERIC_STRING.length());
            builder.append(UrlShortenerConstants.ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private void updateConcurrentURLSet(URLRepository urlRepository) {
        List<UrlLink> urls = urlRepository.findAll();
        for (UrlLink url : urls) {
            concurentURLSet.add(url.getShortURL());
        }
    }

    public boolean isValidURL(String url){
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
