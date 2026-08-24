package org.elis.movieexplorer.repository;

import java.util.List;
import java.util.Optional;

import org.elis.movieexplorer.model.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BigliettoRepository extends JpaRepository<Biglietto, Long>{
	
	@Query("SELECT b FROM Biglietto b JOIN b.utente u WHERE u.id = :id")
	 List<Biglietto> findByIdUtente(Long id);
	
	@Query("SELECT b FROM Biglietto b JOIN b.spettacolo s WHERE s.id = :id")
	Optional<List<Biglietto>> findByIdSpettacolo(Long id);
}
