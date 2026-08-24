package org.elis.movieexplorer.dto.spettacolo.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EditSpettacoloDTO {
	private LocalDate data;
	private LocalDateTime oraInizio;
	private LocalDateTime oraFine;
	private Integer postiRimanenti;
	private List<Long> idBiglietti;
	private Long idSala;
	private Long idFilm;
}
