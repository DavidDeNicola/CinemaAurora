package org.elis.movieexplorer.repository;

import java.util.List;
import java.util.Optional;

import org.elis.movieexplorer.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface FilmRepository extends JpaRepository<Film, Long> {

	Optional<Film> findByTitolo(String titolo);

	@Query("SELECT f FROM Film f JOIN f.generi g WHERE g.id = :id")
	List<Film> findByIdGenere(Long id);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM film_generi WHERE generi_id = :idGenere", nativeQuery = true)
	void removeGenereFromAllFilms(Long idGenere);
	
	boolean existsByImdbID(String imdbID);
}