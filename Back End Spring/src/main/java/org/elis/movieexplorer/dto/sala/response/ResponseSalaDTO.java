package org.elis.movieexplorer.dto.sala.response;

import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.elis.movieexplorer.model.enums.Tipo;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ResponseSalaDTO {
	private Long id;
	
	private String nome;
	
	private Integer numeroPosti;
	
	private Tipo tipo;

	private List<Long> idSpettacoli;
}
