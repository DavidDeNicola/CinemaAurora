package org.elis.movieexplorer.dto.message.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessaggioDTO {
	
	private String messaggio;
	
	private LocalDateTime createdAt;
	
	private boolean isCliente;
}
