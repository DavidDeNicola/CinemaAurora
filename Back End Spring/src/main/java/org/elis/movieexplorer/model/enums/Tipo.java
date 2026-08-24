package org.elis.movieexplorer.model.enums;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Tipo {
	TRED(BigDecimal.valueOf(15.0)),
	IMAX(BigDecimal.valueOf(10.0)),
	NORMALE(BigDecimal.valueOf(7.0));
	
	private final BigDecimal prezzo;
	
	Tipo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
}
