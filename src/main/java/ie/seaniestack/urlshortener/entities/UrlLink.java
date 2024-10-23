package ie.seaniestack.urlshortener.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UrlLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalURL;
    private String shortURL;

    public UrlLink(String originalURL, String shortURL) {
        this.originalURL = originalURL;
        this.shortURL = shortURL;
    }
}