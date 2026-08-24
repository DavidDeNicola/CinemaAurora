package org.elis.movieexplorer.dto.film.request;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
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
public class InsertFilmDTO {
	@NotBlank
	private String titolo;
	@NotBlank
	private String descrizione;
	@NotNull
	@Range(min = 0, max = 500)
	private Integer durata;
	private String imdbID;
	@NotBlank
	private String attori;
	@NotBlank
	@URL
	private String urlLocandina;
	@URL
	private String urlTrailer;
	@NotEmpty
	private List<Long> idGeneri;
}
