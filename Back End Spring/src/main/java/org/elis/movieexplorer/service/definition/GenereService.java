package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.genere.request.EditGenereDTO;
import org.elis.movieexplorer.dto.genere.request.InsertGenereDTO;
import org.elis.movieexplorer.dto.genere.response.ResponseGenereDTO;

public interface GenereService {
	// CREATE
	ResponseGenereDTO insert(InsertGenereDTO dto);
	
	// READ
	List<ResponseGenereDTO> findAll();
	
	ResponseGenereDTO findById(Long id);
	
	List<ResponseGenereDTO> findByIdFilm(Long id);
	
	// UPDATE
	ResponseGenereDTO editById(Long id, EditGenereDTO gMod);
	
	// DELETE
	void removeById(Long id);
}
