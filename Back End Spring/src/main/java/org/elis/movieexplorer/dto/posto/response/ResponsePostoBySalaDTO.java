package org.elis.movieexplorer.dto.posto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ResponsePostoBySalaDTO {
	
	private Long id;
	
    private Integer colonna;

    private String fila;
}
