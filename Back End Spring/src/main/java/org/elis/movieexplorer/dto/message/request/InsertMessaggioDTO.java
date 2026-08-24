package org.elis.movieexplorer.dto.message.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMessaggioDTO {
	@NotEmpty
	@Length(max=500)
	private String messaggio;
	
	@NotNull
	@Positive
	private Long idChat;	
}
