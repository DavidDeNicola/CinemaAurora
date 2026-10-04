package org.elis.movieexplorer.dto.spettacolo.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ResponseSpettacoloDTO {
	private Long id;

	private LocalDate data;

	private LocalDateTime oraInizio;
	
	private LocalDateTime oraFine;
	
	private Integer postiRimanenti;

	private List<Long> idBiglietti;

	private String nomeSala;
	
	private String tipoSala;

	private String nomeFilm;
	
	private Long idFilm;
}
