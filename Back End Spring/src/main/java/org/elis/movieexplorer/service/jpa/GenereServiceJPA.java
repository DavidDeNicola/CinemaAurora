package org.elis.movieexplorer.service.jpa;

import java.util.List;
import java.util.stream.Collectors;

import org.elis.movieexplorer.dto.genere.request.EditGenereDTO;
import org.elis.movieexplorer.dto.genere.request.InsertGenereDTO;
import org.elis.movieexplorer.dto.genere.response.ResponseGenereDTO;
import org.elis.movieexplorer.exception.definition.MEConflictException;
import org.elis.movieexplorer.exception.definition.MENoContentException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.GenereMapper;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Genere;
import org.elis.movieexplorer.repository.FilmRepository;
import org.elis.movieexplorer.repository.GenereRepository;
import org.elis.movieexplorer.service.definition.GenereService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(name="service.impl", havingValue="JPA")
@RequiredArgsConstructor
public class GenereServiceJPA implements GenereService {
	private final GenereRepository repository;
	private final FilmRepository repositoryF;
	private final GenereMapper mapper;
	
	@Override
	public ResponseGenereDTO insert(InsertGenereDTO dto) {
		if(repository.findByNome(dto.getNome()).isPresent()) 
			throw new MEConflictException("genere con nome '"+dto.getNome()+"' già esistente");
		return mapper.toResponse(repository.save(mapper.fromInsert(dto)));
	}

	@Override
	public List<ResponseGenereDTO> findAll() {
		List<Genere> generi = repository.findAll();
		if(generi.isEmpty()) 
			throw new MENoContentException("la lista di generi è vuota");
		return generi.stream().map(g -> mapper.toResponse(g)).collect(Collectors.toList());
	}

	@Override
	public ResponseGenereDTO findById(Long id) {
		Genere g = repository.findById(id).orElseThrow( () -> new MENotFoundException("genere non trovato per id: " + id));
		return mapper.toResponse(g);
	}

	@Override
	public List<ResponseGenereDTO> findByIdFilm(Long id) {
		@SuppressWarnings("unused")
		Film f = repositoryF.findById(id).orElseThrow(() -> new MENotFoundException("film non trovato per id: " + id));
		List<Genere> generi = repository.findByIdFilm(id);
		if(generi.isEmpty()) 
			throw new MENoContentException("lista generi vuota per film con id: " + id);
		return generi.stream().map(g -> mapper.toResponse(g)).collect(Collectors.toList());
	}

	@Override
	public ResponseGenereDTO editById(Long id, EditGenereDTO gMod) {
		Genere g = repository.findById(id).orElseThrow(() -> new MENotFoundException("genere non trovato per id: " + id));
		
		if(gMod.getNome() != null) { 
			if(repository.findByNome(gMod.getNome()).isPresent()) throw new MEConflictException("genere con nome "+gMod.getNome()+" già presente");
			g.setNome(gMod.getNome()); 
			}
		
		if(gMod.getIdFilms() != null) { 	
			List<Film> films = repositoryF.findAllById(gMod.getIdFilms());
			if(films.size() != gMod.getIdFilms().size()) { 
				throw new MENotFoundException("uno o più film non sono stati trovati per gli id forniti");
			}
			g.setFilms(films);
		}
		
		return mapper.toResponse(repository.save(g));
	}

	@Override
	public void removeById(Long id) {
		if(!repository.existsById(id)) 
			throw new MENotFoundException("genere non trovato per id: "+id);
		repositoryF.removeGenereFromAllFilms(id);
		repository.deleteById(id);
	}
}
