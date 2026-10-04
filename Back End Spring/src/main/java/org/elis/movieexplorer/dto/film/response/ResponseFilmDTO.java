package org.elis.movieexplorer.dto.film.response;

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
public class ResponseFilmDTO {
	private Long id;
	
	private String titolo;
	
	private String descrizione;
	
	private Integer durata;
	
	private String attori;
	
	private String urlLocandina;

	private String urlTrailer;
	
	private List<String> nomeGeneri;

}
