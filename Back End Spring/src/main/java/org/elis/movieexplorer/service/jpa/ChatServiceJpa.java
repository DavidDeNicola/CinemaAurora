package org.elis.movieexplorer.service.jpa;

import java.util.List;
import org.elis.movieexplorer.dto.chat.request.InsertChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseChatDTO;
import org.elis.movieexplorer.dto.chat.response.ResponseInfoChatDTO;
import org.elis.movieexplorer.dto.message.request.InsertMessaggioDTO;
import org.elis.movieexplorer.exception.definition.MENotAuthorizedException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.ChatMapper;
import org.elis.movieexplorer.mapper.MessaggioMapper;
import org.elis.movieexplorer.model.Chat;
import org.elis.movieexplorer.model.Messaggio;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.enums.Ruolo;
import org.elis.movieexplorer.model.enums.StatoChat;
import org.elis.movieexplorer.repository.ChatRepository;
import org.elis.movieexplorer.repository.MessaggioRepository;
import org.elis.movieexplorer.service.definition.ChatService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceJpa implements ChatService {
	private final ChatRepository chatRepo;
	private final MessaggioRepository messageRepo;
	private final ChatMapper chatMapper;
	private final MessaggioMapper messageMapper;

	@Override
	@Transactional
	public void insertChat(InsertChatDTO dto, Utente utente) {
		Chat chat = chatMapper.toEntity(dto);
		chat.setUtente(utente);
		chat = chatRepo.save(chat);
		Messaggio message = new Messaggio(
				null,
				dto.getMessaggio(),
				chat.getCreatedAt(),
				chat,
				utente);
		messageRepo.save(message);
	}

	@Override
	@Transactional
	public void insertMessage(InsertMessaggioDTO dto, Utente utente) {
		Chat chat = chatRepo.findById(dto.getIdChat())
				.orElseThrow(
					() -> new MENotFoundException("Chat non trovata.")
			);
		Messaggio message = messageMapper.toEntity(dto, chat, utente);
		messageRepo.save(message);
	}

	@Override
	public List<ResponseInfoChatDTO> findAllChats(Utente utente) {
		List<Chat> chats;
		if(utente.getRuolo() == Ruolo.CLIENTE) {
			chats = chatRepo.findAllByUtenteIdOrderByCreatedAtAsc(utente.getId());
		} else {
			chats = chatRepo.findAllByOrderByCreatedAtAsc();
		}
		
		return chats
				.stream()
				.map(c -> {
					Messaggio ultimoMessaggio = messageRepo.findTopByChatIdOrderByCreatedAtAsc(c.getId()).orElseThrow(() -> new MENotFoundException("messaggio non trovato"));
					boolean messaggiInSospeso;
					if(utente.getRuolo() == Ruolo.CLIENTE) {
						messaggiInSospeso = c.isMessaggiSospesoPerCliente();
					} else {
						messaggiInSospeso = c.isMessaggiSospesoPerStaff();
					}
					return chatMapper.toResponse(c, messaggiInSospeso, ultimoMessaggio);
				})
				.toList();
	}

	@Override
	@Transactional
	public ResponseChatDTO findChatById(Long chatId, Utente utente) {
		Chat chat = chatRepo.findById(chatId)
			.orElseThrow(
				() -> new MENotFoundException("Chat non trovata.")
		);

		if(utente.getId().equals(chat.getUtente().getId())
				|| (utente.getRuolo() == Ruolo.STAFF
				|| utente.getRuolo() == Ruolo.SUPERADMIN)){

			// imposto i messaggi in sospeso a false in base all utenza e stato della chat
			if( utente.getRuolo() == Ruolo.CLIENTE ){
				chat.setMessaggiSospesoPerCliente(false);
				chatRepo.save(chat);
			}
			else{
				if( chat.getStato() != StatoChat.IN_ATTESA ){
					chat.setMessaggiSospesoPerStaff(false);
					chatRepo.save(chat);
				}
			}

			List<Messaggio> messaggi = messageRepo.findByChatIdOrderByCreatedAtAsc(chatId);

            return chatMapper.toResponse(chat, messaggi);

		}else{
			throw new MENotAuthorizedException("Non hai accesso a questo contenuto.");
		}
	}

	@Override
	@Transactional
	public void cambiaStatoChat(Long idChat) {
		Chat chat = chatRepo.findById(idChat)
			.orElseThrow(
				() -> new MENotFoundException("Chat non trovata.")
		);
		chat.setStato(
				chat.getStato()==StatoChat.IN_ATTESA?
						StatoChat.APERTO:StatoChat.CHIUSO
		);
		chatRepo.save(chat);
	}

	@Override
	public Integer countChatNotRead(Utente utente){
		return utente.getRuolo() == Ruolo.CLIENTE?
				chatRepo.countNotReadedChatForUser(utente.getId()):
				chatRepo.countNotReadedChatForStaff();
	}
}
