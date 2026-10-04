package org.elis.movieexplorer.dto.sala.response;

import java.util.List;

import org.elis.movieexplorer.model.enums.Tipo;

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
public class ResponseSalaDTO {
	private Long id;
	
	private String nome;
	
	private Integer numeroPosti;
	
	private Tipo tipo;

	private List<Long> idSpettacoli;
}
