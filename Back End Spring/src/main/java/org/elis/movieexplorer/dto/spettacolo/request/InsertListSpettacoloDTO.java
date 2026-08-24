package org.elis.movieexplorer.dto.spettacolo.request;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class InsertListSpettacoloDTO {
	@NotEmpty
	private List<@FutureOrPresent LocalDate> date; // 1-2-3-6-7-8 di gennaio
	
	@NotEmpty
	private HashMap<@Future LocalDateTime, @Future LocalDateTime> orariInizioFine; // (10-11.30) (12-14) (15-16.30) (17-18.30) orari
	
	@NotNull
	private Long idSala;
	
	@NotNull
	private Long idFilm;
}
