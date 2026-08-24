package org.elis.movieexplorer.repository;

import java.util.List;
import java.util.Optional;

import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
	
	Optional<Sala> findByNome(String nome);

	// TODO IMPLEMENTARE QUERY?
	List<Sala> findByTipo(Tipo tipo);
	
}
