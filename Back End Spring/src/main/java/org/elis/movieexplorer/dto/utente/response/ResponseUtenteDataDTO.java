package org.elis.movieexplorer.dto.utente.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseUtenteDataDTO {
    private Long id;

    private String nome;

    private String cognome;

    private String email;
}
