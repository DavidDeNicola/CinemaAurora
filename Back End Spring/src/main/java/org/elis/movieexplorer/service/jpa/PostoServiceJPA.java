package org.elis.movieexplorer.service.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySalaDTO;
import org.elis.movieexplorer.dto.posto.response.ResponsePostoBySpettacoloDTO;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.PostoMapper;
import org.elis.movieexplorer.model.Posto;
import org.elis.movieexplorer.model.Spettacolo;
import org.elis.movieexplorer.repository.PostoRepository;
import org.elis.movieexplorer.repository.SpettacoloRepository;
import org.elis.movieexplorer.service.definition.PostoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(name = "service.impl", havingValue = "JPA")
@RequiredArgsConstructor
public class PostoServiceJPA implements PostoService {

	private final PostoRepository repository;
	private final SpettacoloRepository spettacoloRepository;
	private final PostoMapper mapper;

	@Override
	public List<ResponsePostoBySalaDTO> findBySala(Long idSala) {
		List<Posto> posti = repository.findBySala(idSala);
		return mapper.toResponse(posti);
	}

	@Override
	public List<ResponsePostoBySpettacoloDTO> findBySpettacolo(Long idSpettacolo) {
		Spettacolo spettacolo = spettacoloRepository.findById(idSpettacolo)
				.orElseThrow(() -> new MENotFoundException("Spettacolo non trovato con id " + idSpettacolo));

		// Tutti i posti della sala in cui si svolge lo spettacolo
		List<Posto> posti = repository.findBySala(spettacolo.getSala().getId());

		// Id dei posti già occupati da un biglietto di questo spettacolo
		Set<Long> idPostiOccupati = new HashSet<>(repository.findIdPostiOccupatiBySpettacolo(idSpettacolo));

		return mapper.toResponseBySpettacolo(posti, idPostiOccupati);
	}

}
