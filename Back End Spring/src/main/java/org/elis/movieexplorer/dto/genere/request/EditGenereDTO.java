package org.elis.movieexplorer.dto.genere.request;

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
public class EditGenereDTO {
	private String nome;
	private List<Long> idFilms;
}
