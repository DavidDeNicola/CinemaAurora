package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySalaDTO;
import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySpettacoloDTO;

public interface PostoService {

	//READ

	/**
	 * Restituisce tutti i posti di una sala specifica.
	 * @param idSala id della sala
	 */
	List<ResponsePostoBySalaDTO> findBySala(Long idSala);

	/**
	 * Restituisce tutti i posti della sala in cui si svolge lo spettacolo indicato,
	 * indicando per ciascuno se è occupato (true se associato a un biglietto di
	 * quello spettacolo) o libero.
	 * @param idSpettacolo id dello spettacolo
	 */
	List<ResponsePostoBySpettacoloDTO> findBySpettacolo(Long idSpettacolo);

}
