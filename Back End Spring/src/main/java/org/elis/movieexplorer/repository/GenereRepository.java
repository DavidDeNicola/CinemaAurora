package org.elis.movieexplorer.repository;

import java.util.List;
import java.util.Optional;

import org.elis.movieexplorer.model.Genere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenereRepository extends JpaRepository<Genere, Long> {
	Optional<Genere> findByNome(String nome);
	
	@Query("SELECT g FROM Genere g JOIN g.films f WHERE f.id = :id")
	List<Genere> findByIdFilm(Long id);
}
