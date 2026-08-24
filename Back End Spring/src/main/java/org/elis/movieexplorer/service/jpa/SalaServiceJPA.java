package org.elis.movieexplorer.service.jpa;

import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.exception.definition.MENoContentException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.SalaMapper;
import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.enums.Tipo;
import org.elis.movieexplorer.repository.SalaRepository;
import org.elis.movieexplorer.service.definition.SalaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "service.impl", havingValue = "JPA")
@RequiredArgsConstructor
public class SalaServiceJPA implements SalaService {
	private final SalaRepository repository;
	private final SalaMapper mapper;	

	@Override
	public List<ResponseSalaDTO> findAll() {
		List<Sala> sale = repository.findAll();
		if(sale.isEmpty()) throw new MENoContentException("la lista di sale è vuota");
		return sale.stream()
				.map(s -> mapper.toResponse(s))
				.collect(Collectors.toList());
	}

	@Override
	public ResponseSalaDTO findById(Long id) {
		Optional<Sala> optS = repository.findById(id);
		Sala s = optS.orElseThrow(() -> new MENotFoundException("sala non trovata per id: " + id));
		return mapper.toResponse(s);
	}

	@Override
	public List<ResponseSalaDTO> findByTipo(Tipo tipo) {
		List<Sala> sale = repository.findByTipo(tipo);
		if(sale.isEmpty()) throw new MENoContentException("nessuna sala trovata per tipo: " + tipo);
		return sale.stream()
				   .map(s -> mapper.toResponse(s))
				   .collect(Collectors.toList());
	}
	
	@Override
	public ResponseSalaDTO findByNome(String nome) {
		Sala sala = repository.findByNome(nome)
				.orElseThrow(() -> new MENotFoundException("sala non trovata per nome: " + nome));	
		return mapper.toResponse(sala);
	}

}
