package org.elis.movieexplorer.dto.chat.response;

import java.util.List;

import org.elis.movieexplorer.dto.message.response.ResponseMessaggioDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseChatDTO {
	
	private ResponseInfoChatDTO chat;
	
	private List<ResponseMessaggioDTO> messaggi;
}
