package org.elis.movieexplorer.mapper;

import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.model.Posto;
import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.Spettacolo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalaMapper {
	
	
	public ResponseSalaDTO toResponse(Sala s) {
		ResponseSalaDTO dto = new ResponseSalaDTO();
		dto.setId(s.getId());
		dto.setNome(s.getNome());
		dto.setTipo(s.getTipo());
		if (s.getSpettacoli() != null) {
	        dto.setIdSpettacoli(s.getSpettacoli().stream()
	                .map(Spettacolo::getId)
	                .toList());
	    }
		dto.setNumeroPosti(s.getPosti().size());
		return dto;
	}
}
