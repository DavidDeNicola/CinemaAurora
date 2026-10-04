package org.elis.movieexplorer.dto.genere.response;

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
public class ResponseGenereDTO {
	private Long id;
	
	private String nome;
	
	private List<String> nomeFilms;
}
