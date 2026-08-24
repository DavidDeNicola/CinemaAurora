package org.elis.movieexplorer.dto.resetPassword.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditPasswordRequest {

	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_#@$!%*?&])[A-Za-z\\d@$#_!%*?&]{8,}$",
	        message = "La password inserita non è valida.")
	private String passwordVecchia;
	
	@NotBlank
	@Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_#@$!%*?&])[A-Za-z\\d@$#_!%*?&]{8,}$",
	        message = "La password inserita non è valida.")
	private String passwordNuova;
	
}
