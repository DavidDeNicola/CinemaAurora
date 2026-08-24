package org.elis.movieexplorer.repository;

import jakarta.validation.constraints.NotBlank;
import org.elis.movieexplorer.model.Token;
import org.elis.movieexplorer.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t.utente from Token t where t.token=:token")
    Optional<Utente> getUtenteByToken(@NotBlank String token);

    Optional<Token> getTokenByUtente_Id(Long utenteId);
}
