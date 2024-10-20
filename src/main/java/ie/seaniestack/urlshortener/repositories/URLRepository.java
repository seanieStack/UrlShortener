package ie.seaniestack.urlshortener.repositories;

import ie.seaniestack.urlshortener.entities.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<URL, Long> {
    Optional<URL> findByShortURL(String shortURL);

    Optional<URL> findByOriginalURL(String originalURL);
}
