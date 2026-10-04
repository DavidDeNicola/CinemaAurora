package org.elis.movieexplorer.dto.sala.request;


import org.elis.movieexplorer.model.enums.Tipo;

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
public class EditSalaDTO {
	private String nome;
	private Tipo tipo;
}
