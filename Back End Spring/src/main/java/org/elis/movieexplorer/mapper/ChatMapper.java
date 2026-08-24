package org.elis.movieexplorer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.elis.movieexplorer.dto.chat.request.InsertChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseInfoChatDTO;
import org.elis.movieexplorer.dto.message.response.ResponseMessaggioDTO;
import org.elis.movieexplorer.model.Chat;
import org.elis.movieexplorer.model.Messaggio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMapper {
	
	private final MessaggioMapper messaggioMapper;
	
	public Chat toEntity(InsertChatDTO dto) {
		Chat chat = new Chat();		
		chat.setOggetto(dto.getOggetto());		
		return chat;		
	}
	
	// FATTo
	public ResponseChatDTO toResponse(Chat chat, List<Messaggio> messaggi) {
		ResponseChatDTO dto = new ResponseChatDTO();
		dto.setChat(toResponse(chat, false, null));
		List<ResponseMessaggioDTO> messaggiDTO = new ArrayList<>();
		
		for(Messaggio m : messaggi) {
			messaggiDTO.add(messaggioMapper.toResponse(								
					m, 
					m.getMittente().getId()
					.equals(chat.getUtente().getId())? true : false));
		}
		
		dto.setMessaggi(messaggiDTO);
		
		return dto;

	}
	
	// FATTO
	public ResponseInfoChatDTO toResponse(Chat chat, Boolean messaggiInSospeso, Messaggio ultimoMessaggio) {
		ResponseInfoChatDTO dto = new ResponseInfoChatDTO();
		
		dto.setId(chat.getId());
		dto.setOggetto(chat.getOggetto());
		dto.setStato(chat.getStato().name());
		dto.setNome(chat.getUtente().getNome());
		dto.setCognome(chat.getUtente().getCognome());
		if(ultimoMessaggio != null) {
				dto.setUltimoMessaggio(
						messaggioMapper.toResponse(
								ultimoMessaggio, 
								ultimoMessaggio.getMittente().getId()
											.equals(chat.getUtente().getId())? true : false));
		}
		if(messaggiInSospeso != null) {
			dto.setMessaggiInSospeso(messaggiInSospeso);
		}
		
		return dto;
	}

}
