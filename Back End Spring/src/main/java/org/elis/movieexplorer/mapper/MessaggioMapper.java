package org.elis.movieexplorer.mapper;

import org.elis.movieexplorer.dto.message.request.InsertMessaggioDTO;
import org.elis.movieexplorer.dto.message.response.ResponseMessaggioDTO;
import org.elis.movieexplorer.model.Chat;
import org.elis.movieexplorer.model.Messaggio;
import org.elis.movieexplorer.model.Utente;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MessaggioMapper {
	public Messaggio toEntity(InsertMessaggioDTO dto, Chat chat, Utente mittente) {
		Messaggio m = new Messaggio();
		
		m.setMessaggio(dto.getMessaggio());
		m.setChat(chat);
		m.setMittente(mittente);
		
		return m;
	}
	
	public ResponseMessaggioDTO toResponse(Messaggio m, boolean isCliente) {
		ResponseMessaggioDTO dto = new ResponseMessaggioDTO();
		
		dto.setMessaggio(m.getMessaggio());
		dto.setCreatedAt(m.getCreatedAt());
		dto.setCliente(isCliente);
		
		return dto;
	}
}
