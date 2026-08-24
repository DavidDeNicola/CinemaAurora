package org.elis.movieexplorer.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySalaDTO;
import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySpettacoloDTO;
import org.elis.movieexplorer.model.Posto;
import org.springframework.stereotype.Component;

@Component
public class PostoMapper {
	
	public List<ResponsePostoBySalaDTO> toResponse(List<Posto> posti) {
		List<ResponsePostoBySalaDTO> listaDTO = new ArrayList<>();
		for(Posto p : posti) {
			ResponsePostoBySalaDTO dto = new ResponsePostoBySalaDTO();
			dto.setId(p.getId());
			dto.setColonna(p.getColonna());
			dto.setFila(String.valueOf((char) (p.getFila() + 65)));
			listaDTO.add(dto);
		}
		return listaDTO;
	}

	public List<ResponsePostoBySpettacoloDTO> toResponseBySpettacolo(List<Posto> posti, Set<Long> idPostiOccupati) {
		List<ResponsePostoBySpettacoloDTO> listaDTO = new ArrayList<>();
		for(Posto p : posti) {
			ResponsePostoBySpettacoloDTO dto = new ResponsePostoBySpettacoloDTO();
			dto.setId(p.getId());
			dto.setColonna(p.getColonna());
			dto.setFila(String.valueOf((char) (p.getFila() + 65)));
			dto.setOccupato(idPostiOccupati.contains(p.getId()));
			listaDTO.add(dto);
		}
		return listaDTO;
	}

}
