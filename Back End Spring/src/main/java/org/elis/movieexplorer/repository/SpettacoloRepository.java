package org.elis.movieexplorer.repository;

import java.time.LocalDate;
import java.util.List;

import org.elis.movieexplorer.model.Spettacolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpettacoloRepository extends JpaRepository<Spettacolo, Long> {

	@Query("SELECT s FROM Spettacolo s WHERE s.sala.id = :idSala AND s.data = :data")
	List<Spettacolo> findSpettacoliInSalaPerData(Long idSala, LocalDate data);

	@Query("SELECT s FROM Spettacolo s WHERE s.data = :data ORDER BY s.oraInizio ASC")
	List<Spettacolo> findByDataOrderByOraInizioAsc(LocalDate data);

	
	@Query("SELECT s FROM Spettacolo s WHERE s.film.id = :id")
	List<Spettacolo> findByIdFilm(Long id);
	
	@Modifying
	@Query("DELETE FROM Spettacolo s WHERE s.film.id = :id")
	void deleteAllByFilmId(Long id);

}
