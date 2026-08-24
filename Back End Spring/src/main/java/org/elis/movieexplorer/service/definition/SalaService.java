package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.sala.request.EditSalaDTO;
import org.elis.movieexplorer.dto.sala.request.InsertSalaDTO;
import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.model.enums.Tipo;



public interface SalaService {
	// CREATE

	// READ
	List<ResponseSalaDTO> findAll();
		
	ResponseSalaDTO findById(Long id);
		
	List<ResponseSalaDTO> findByTipo(Tipo tipo);
	
	ResponseSalaDTO findByNome(String nome);
		
	// UPDATE
		
	// DELETE
}
