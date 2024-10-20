package ie.seaniestack.urlshortener.util;

import ie.seaniestack.urlshortener.entities.URL;
import ie.seaniestack.urlshortener.repositories.URLRepository;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class URLUtils {

    private static final Set<String> concurentURLSet = ConcurrentHashMap.newKeySet();
    private static final int urlLength = 7;

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
        String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < urlLength; i++) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private void updateConcurrentURLSet(URLRepository urlRepository) {
        List<URL> urls = urlRepository.findAll();
        for (URL url : urls) {
            concurentURLSet.add(url.getShortURL());
        }
    }
}
