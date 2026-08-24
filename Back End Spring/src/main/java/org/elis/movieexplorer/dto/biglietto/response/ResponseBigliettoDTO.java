package org.elis.movieexplorer.dto.biglietto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ResponseBigliettoDTO {
	
	private Long id;
	
	private String nomeUtente;
	
	private Long idSpettacolo;
	
	private String fila;

	private Integer colonna;
	
	private String codiceBiglietto;
	
	private BigDecimal prezzo;
}
