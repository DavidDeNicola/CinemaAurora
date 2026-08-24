package org.elis.movieexplorer.dto.sala.request;


import org.elis.movieexplorer.model.enums.Tipo;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
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
public class InsertSalaDTO {
	@NotBlank
	private String nome;
	
	@NotNull
	@Range(min = 0)
	private Short numeroPosti;
	
	@NotNull
	private Tipo tipo;
}
