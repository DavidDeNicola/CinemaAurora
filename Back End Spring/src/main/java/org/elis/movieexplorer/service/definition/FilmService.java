package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.film.request.EditFilmDTO;
import org.elis.movieexplorer.dto.film.request.InsertFilmDTO;
import org.elis.movieexplorer.dto.film.response.ResponseFilmDTO;
import org.elis.movieexplorer.dto.omdbapi.response.LongOmdbResponseApiDTO;

public interface FilmService {
	// CREATE
	ResponseFilmDTO insert(InsertFilmDTO dto);
	
	// READ
	List<ResponseFilmDTO> findAll();
	
	ResponseFilmDTO findById(Long id);
	
	List<ResponseFilmDTO> findByIdGenere(Long id);
	
	// UPDATE
	ResponseFilmDTO editById(Long id, EditFilmDTO fMod);
	
	// DELETE
	void removeById(Long id);

    List<LongOmdbResponseApiDTO> findByTitolo(String titolo);

	Boolean findByImdbID(String id);
}
