package org.elis.movieexplorer.dto.chat.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertChatDTO {
	@NotNull
	@Length(max = 64)
	private String oggetto;
	
	@NotNull
	@Length(max = 500)
	private String messaggio;
}
