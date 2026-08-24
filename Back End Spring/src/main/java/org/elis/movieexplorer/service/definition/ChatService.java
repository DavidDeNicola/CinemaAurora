package org.elis.movieexplorer.service.definition;

import java.util.List;
import org.elis.movieexplorer.dto.chat.request.InsertChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseInfoChatDTO;
import org.elis.movieexplorer.dto.message.request.InsertMessaggioDTO;
import org.elis.movieexplorer.model.Utente;

public interface ChatService {

	void insertChat(InsertChatDTO dto, Utente utente);

	void insertMessage(InsertMessaggioDTO dto, Utente utente);

	List<ResponseInfoChatDTO> findAllChats(Utente utente);

	ResponseChatDTO findChatById(Long idChat, Utente utente);

	void cambiaStatoChat(Long idChat);

	Integer countChatNotRead(Utente u);
}
