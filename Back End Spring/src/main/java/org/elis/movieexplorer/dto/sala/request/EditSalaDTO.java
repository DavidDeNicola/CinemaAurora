package org.elis.movieexplorer.dto.sala.request;


import lombok.*;
import org.elis.movieexplorer.model.enums.Tipo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EditSalaDTO {
	private String nome;
	private Tipo tipo;
}
