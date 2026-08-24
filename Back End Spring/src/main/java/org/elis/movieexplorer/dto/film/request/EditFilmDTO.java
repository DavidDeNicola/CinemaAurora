package org.elis.movieexplorer.dto.film.request;

import java.util.List;

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
public class EditFilmDTO {
	private String titolo;
	private String descrizione;
	private Integer durata;
	private String attori;
	private String urlLocandina;
	private String urlTrailer;
	private List<Long> idGeneri;
	private List<Long> idSpettacoli;
}
