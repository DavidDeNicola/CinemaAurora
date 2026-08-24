package org.elis.movieexplorer.repository;

import jakarta.validation.constraints.NotBlank;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.enums.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findUtenteByEmail(String email);
    
    List<Utente> findAllByRuolo(Ruolo ruolo);
    
}
