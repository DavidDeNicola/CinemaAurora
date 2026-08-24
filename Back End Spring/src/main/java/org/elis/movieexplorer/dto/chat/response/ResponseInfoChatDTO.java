package org.elis.movieexplorer.dto.chat.response;

import org.elis.movieexplorer.dto.message.response.ResponseMessaggioDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseInfoChatDTO {
	private Long id;
	
	private String oggetto;
	
	private String stato;
	
	private Boolean messaggiInSospeso;
	
	private String nome;
	
	private String cognome;
		
	private ResponseMessaggioDTO ultimoMessaggio;
}
