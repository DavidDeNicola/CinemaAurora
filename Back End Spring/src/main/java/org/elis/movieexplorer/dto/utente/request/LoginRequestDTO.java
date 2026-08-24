package org.elis.movieexplorer.dto.utente.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci una mail valida")
    private String email;
    
    @NotBlank(message = "La password è obbligatoria")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_#@$!%*?&])[A-Za-z\\d@$#_!%*?&]{8,}$",
            message = "La password inserita non è valida.")
    private String password;
}

