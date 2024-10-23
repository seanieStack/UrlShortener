package ie.seaniestack.urlshortener.repositories;

import ie.seaniestack.urlshortener.entities.UrlLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<UrlLink, Long> {
    Optional<UrlLink> findByShortURL(String shortURL);

    Optional<UrlLink> findByOriginalURL(String originalURL);
}
