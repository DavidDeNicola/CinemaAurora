package org.elis.movieexplorer.service.omdb;

import org.elis.movieexplorer.dto.omdbapi.response.LongOmdbResponseApiDTO;
import org.elis.movieexplorer.dto.omdbapi.response.ShortOmdbResponseApiDTO;
import org.elis.movieexplorer.dto.omdbapi.response.ShortOmdbResponseDTO;
import org.elis.movieexplorer.exception.definition.MEBaseException;
import org.elis.movieexplorer.exception.definition.MENotAuthorizedException;
import org.elis.movieexplorer.exception.definition.MEUnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OmdbService {
    private final RestClient restClient;

    public OmdbService(){
        restClient = RestClient
                .builder()
                .baseUrl("https://www.omdbapi.com")
                .build();
    }

    public List<LongOmdbResponseApiDTO> getFilmOMDB(String title, int page) throws MEBaseException {

        ShortOmdbResponseApiDTO response = restClient.get()
                .uri(t ->
                        t.queryParam("apikey", "175ca862")
                         .queryParam("s", title)
                         .queryParam("page", page)
                         .queryParam("type","movie").build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(t ->
                    t.isSameCodeAs(HttpStatus.UNAUTHORIZED), (req, res) -> {
                        throw new MENotAuthorizedException("Non sei autorizzato");
                })
                .body(ShortOmdbResponseApiDTO.class);

        List<LongOmdbResponseApiDTO> longResponse = new ArrayList<>();

        if(response.getResponse().equals("True")){
            for(ShortOmdbResponseDTO s:response.getSearch()){
                LongOmdbResponseApiDTO l = getFilmOMDBAllDetails(s.getTitle());
                longResponse.add(l);
            }
        }
        return longResponse;
    }

    public LongOmdbResponseApiDTO getFilmOMDBAllDetails(String title){
        return restClient.get()
                .uri(t ->
                        t.queryParam("apikey", "175ca862")
                          .queryParam("t", title).build())
                .retrieve()
                .onStatus(t ->
                        t.isSameCodeAs(HttpStatus.UNAUTHORIZED), (req, res) -> {
                    throw new MENotAuthorizedException("Non sei autorizzato");
                })
                .body(LongOmdbResponseApiDTO.class);
    }
}
