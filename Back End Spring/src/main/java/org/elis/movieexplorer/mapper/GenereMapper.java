package org.elis.movieexplorer.mapper;

import java.util.stream.Collectors;

import org.elis.movieexplorer.dto.genere.request.EditGenereDTO;
import org.elis.movieexplorer.dto.genere.request.InsertGenereDTO;
import org.elis.movieexplorer.dto.genere.response.ResponseGenereDTO;
import org.elis.movieexplorer.model.Genere;
import org.springframework.stereotype.Component;

@Component
public class GenereMapper {
	public Genere fromInsert(InsertGenereDTO dto) {
		Genere g = new Genere();
		g.setNome(dto.getNome());
		return g;
	}
	
	public Genere fromEdit(EditGenereDTO dto) {
		Genere g = new Genere();
		g.setNome(dto.getNome());
		return g;
	}
	
	public ResponseGenereDTO toResponse(Genere g) {
		ResponseGenereDTO dto = new ResponseGenereDTO();
		dto.setId(g.getId());
		dto.setNome(g.getNome());
		dto.setNomeFilms(g.getFilms().stream().map(f -> f.getTitolo()).collect(Collectors.toList()));
		return dto;
	}
}
