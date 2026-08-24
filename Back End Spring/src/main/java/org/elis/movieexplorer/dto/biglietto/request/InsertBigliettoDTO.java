package org.elis.movieexplorer.dto.biglietto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InsertBigliettoDTO {

	@Positive
	private Long idSpettacolo;
	
	private List<@Positive Integer> idPosti;
}
