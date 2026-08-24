package org.elis.movieexplorer.dto.resetPassword.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {

	@NotBlank
	private String token;
	
	@NotBlank
	@Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_#@$!%*?&])[A-Za-z\\d@$#_!%*?&]{8,}$",
	        message = "La password inserita non è valida.")
	private String password;
	
	@NotBlank
	@Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_#@$!%*?&])[A-Za-z\\d@$#_!%*?&]{8,}$",
	        message = "La password inserita non è valida.")
	private String confermaPassword;
	
}