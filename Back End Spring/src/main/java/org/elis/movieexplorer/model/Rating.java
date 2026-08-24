package org.elis.movieexplorer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @JsonProperty("Source")
    private String source;

    @JsonProperty("Value")
    private String value;
}
