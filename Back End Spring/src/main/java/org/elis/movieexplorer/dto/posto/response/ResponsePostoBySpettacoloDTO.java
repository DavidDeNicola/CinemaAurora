package org.elis.movieexplorer.dto.posto.response;

import java.util.List;

import org.elis.movieexplorer.model.enums.Tipo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ResponsePostoBySpettacoloDTO {
	
	private Long id;
	
    private Integer colonna;

    private String fila;

    private boolean occupato;

}
