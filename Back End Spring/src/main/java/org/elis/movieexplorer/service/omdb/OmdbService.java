package org.elis.movieexplorer.service.omdb;

import java.util.ArrayList;
import java.util.List;

import org.elis.movieexplorer.dto.omdbapi.response.LongOmdbResponseApiDTO;
import org.elis.movieexplorer.dto.omdbapi.response.ShortOmdbResponseApiDTO;
import org.elis.movieexplorer.dto.omdbapi.response.ShortOmdbResponseDTO;
import org.elis.movieexplorer.exception.definition.MEBaseException;
import org.elis.movieexplorer.exception.definition.MENotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OmdbService {
    private final RestClient restClient;
    private final String apiKey;

    public OmdbService(@Value("${omdb.api-key}") String apiKey) {
        this.apiKey = apiKey;
        restClient = RestClient
                .builder()
                .baseUrl("https://www.omdbapi.com")
                .build();
    }

    public List<LongOmdbResponseApiDTO> getFilmOMDB(String title, int page) throws MEBaseException {

        ShortOmdbResponseApiDTO response = restClient.get()
                .uri(t -> t.queryParam("apikey", apiKey)
                        .queryParam("s", title)
                        .queryParam("page", page)
                        .queryParam("type", "movie").build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(t -> t.isSameCodeAs(HttpStatus.UNAUTHORIZED), (req, res) -> {
                    throw new MENotAuthorizedException("Non sei autorizzato");
                })
                .body(ShortOmdbResponseApiDTO.class);

        List<LongOmdbResponseApiDTO> longResponse = new ArrayList<>();

        if (response.getResponse().equals("True")) {
            for (ShortOmdbResponseDTO s : response.getSearch()) {
                LongOmdbResponseApiDTO l = getFilmOMDBAllDetails(s.getTitle());
                longResponse.add(l);
            }
        }
        return longResponse;
    }

    public LongOmdbResponseApiDTO getFilmOMDBAllDetails(String title) {
        return restClient.get()
                .uri(t -> t.queryParam("apikey", apiKey)
                        .queryParam("t", title).build())
                .retrieve()
                .onStatus(t -> t.isSameCodeAs(HttpStatus.UNAUTHORIZED), (req, res) -> {
                    throw new MENotAuthorizedException("Non sei autorizzato");
                })
                .body(LongOmdbResponseApiDTO.class);
    }
}
