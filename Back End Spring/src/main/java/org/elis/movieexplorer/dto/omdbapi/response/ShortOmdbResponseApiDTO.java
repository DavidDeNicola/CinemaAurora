package org.elis.movieexplorer.dto.omdbapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShortOmdbResponseApiDTO {

    @JsonProperty("Search")
    private List<ShortOmdbResponseDTO> search;
    private String totalResults;
    @JsonProperty("Response")
    private String response;
}
