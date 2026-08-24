package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.biglietto.request.InsertBigliettoDTO;
import org.elis.movieexplorer.dto.biglietto.response.ResponseBigliettoDTO;
import org.elis.movieexplorer.model.Biglietto;
import org.elis.movieexplorer.model.Utente;

public interface BigliettoService {
	// CREATE
	List<ResponseBigliettoDTO> insert(InsertBigliettoDTO dto, Utente utenteLoggato);
	
	// READ
	List<ResponseBigliettoDTO> findByIdUtente(Long idUtente);
	
	List<ResponseBigliettoDTO> findByIdSpettacolo(Long idSpettacolo);
	
	// UPDATE
	//----
	
	// DELETE
	void removeByIdEmail(Long idBiglietto, String email);
}