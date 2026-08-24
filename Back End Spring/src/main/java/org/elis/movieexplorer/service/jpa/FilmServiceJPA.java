package org.elis.movieexplorer.service.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elis.movieexplorer.dto.film.request.EditFilmDTO;
import org.elis.movieexplorer.dto.film.request.InsertFilmDTO;
import org.elis.movieexplorer.dto.film.response.ResponseFilmDTO;
import org.elis.movieexplorer.dto.omdbapi.response.LongOmdbResponseApiDTO;
import org.elis.movieexplorer.exception.definition.MEConflictException;
import org.elis.movieexplorer.exception.definition.MENoContentException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.FilmMapper;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Genere;
import org.elis.movieexplorer.repository.FilmRepository;
import org.elis.movieexplorer.repository.GenereRepository;
import org.elis.movieexplorer.repository.SpettacoloRepository;
import org.elis.movieexplorer.service.definition.FilmService;
import org.elis.movieexplorer.service.omdb.OmdbService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(name="service.impl", havingValue="JPA")
@RequiredArgsConstructor
public class FilmServiceJPA implements FilmService {

	private final FilmRepository repository;
	private final GenereRepository repositoryG;
	private final SpettacoloRepository spettacoloRepository;
	private final FilmMapper mapper;
	private final OmdbService omdbService;

	@Override
	public ResponseFilmDTO insert(InsertFilmDTO dto) {
		if(repository.findByTitolo(dto.getTitolo()).isPresent()) 
			throw new MEConflictException("film con quel titolo già esistente"); 
		
		List<Genere> generi = repositoryG.findAllById(dto.getIdGeneri());
		Film f = mapper.fromInsert(dto, generi);
		
		return mapper.toResponse(repository.save(f));
	}

	@Override
	public List<ResponseFilmDTO> findAll() {
		List<Film> films = repository.findAll();
		
		if(films.isEmpty()) 
			throw new MENoContentException("la lista di film è vuota");
		
		return films.stream().map(f -> mapper.toResponse(f)).collect(Collectors.toList());
	}

	@Override
	public ResponseFilmDTO findById(Long id) {	
		Film f = repository.findById(id).orElseThrow(() -> new MENotFoundException("id non trovato"));
		return mapper.toResponse(f);
	}

	@Override
	public List<ResponseFilmDTO> findByIdGenere(Long id) {	
		@SuppressWarnings("unused")
		Genere g = repositoryG.findById(id).orElseThrow(() -> new MENotFoundException("genere non trovato per id: " + id));
		
		List<Film> films = repository.findByIdGenere(id);
		
		if(films.isEmpty()) throw new MENoContentException("nessun film trovato per genere con id: " + id);
		
		return films.stream().map(f -> mapper.toResponse(f)).collect(Collectors.toList());
	}

	@Override
	public ResponseFilmDTO editById(Long id, EditFilmDTO fMod) {	
		Film f = repository.findById(id).orElseThrow(() -> new MENotFoundException("film non trovato per id: " + id));
		if(fMod.getTitolo() != null) { 
			if(repository.findByTitolo(fMod.getTitolo()).isPresent()) 
				throw new MEConflictException("film con titolo "+fMod.getTitolo()+" già esistente"); 
			f.setTitolo(fMod.getTitolo()); 
		}
		if(fMod.getDescrizione() != null) f.setDescrizione(fMod.getDescrizione());
		if(fMod.getDurata() != null) f.setDurata(fMod.getDurata());
		if(fMod.getAttori() != null) f.setAttori(fMod.getAttori());
		if(fMod.getUrlLocandina() != null) f.setUrlLocandina(fMod.getUrlLocandina());
		
		if(fMod.getIdGeneri() != null) { 	
			List<Genere> generi = repositoryG.findAllById(fMod.getIdGeneri());
			if(generi.size()!= fMod.getIdGeneri().size()) {
				throw new MENotFoundException("uno o più generi non sono stati trovati per gli id forniti");
			}
			f.setGeneri(generi);
		}
		
		return mapper.toResponse(repository.save(f));
	}

	@Override
	@Transactional
	public void removeById(Long id) {
		@SuppressWarnings("unused")
		Film daRimuovere = repository.findById(id)
			.orElseThrow(() -> new MENotFoundException("film non trovato per id: " + id));
		spettacoloRepository.deleteAllByFilmId(id);
		repository.deleteById(id);
	}

	@Override
	public List<LongOmdbResponseApiDTO> findByTitolo(String titolo){
	
		
		List<LongOmdbResponseApiDTO> omdbResponse = omdbService.getFilmOMDB(titolo,1);
		if (omdbResponse != null) {
			return omdbResponse;
		}
		
		
		throw new MENoContentException("Nessun film trovato.");
	}

	@Override
	public Boolean findByImdbID(String id) {
		return repository.existsByImdbID(id);
	}
}