package org.elis.movieexplorer.service.jpa;

import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.dto.spettacolo.request.EditSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.request.InsertSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.response.ResponseSpettacoloDTO;
import org.elis.movieexplorer.exception.definition.MEBadRequestException;
import org.elis.movieexplorer.exception.definition.MEConflictException;
import org.elis.movieexplorer.exception.definition.MENoContentException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.mapper.SpettacoloMapper;
import org.elis.movieexplorer.model.Biglietto;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.Spettacolo;
import org.elis.movieexplorer.repository.*;
import org.elis.movieexplorer.service.definition.SpettacoloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "service.impl", havingValue = "JPA")
@RequiredArgsConstructor
public class SpettacoloServiceJPA implements SpettacoloService {
	private final SpettacoloRepository repository;
	private final SalaRepository repositoryS;
	private final FilmRepository repositoryF;
	private final BigliettoRepository repositoryB;
	private final PostoRepository repositoryP;
	private final SpettacoloMapper mapper;

	@Override
	public ResponseSpettacoloDTO insert(InsertSpettacoloDTO dto) {
		if (!dto.getOraInizio().toLocalDate().equals(dto.getData())) {
			throw new MEBadRequestException("la data dello spettacolo deve coincidere con la data dell'orario di inizio");
		}

		Sala sala = repositoryS.findById(dto.getIdSala())
				.orElseThrow(() -> new MENotFoundException("sala non trovata per id: " + dto.getIdSala()));
		Film film = repositoryF.findById(dto.getIdFilm())
				.orElseThrow(() -> new MENotFoundException("film non trovato per id: " + dto.getIdFilm()));

		LocalDateTime oraFine = dto.getOraInizio().plusMinutes(film.getDurata());

		if (!dto.getOraInizio().isBefore(oraFine)) {
			throw new MEBadRequestException("l'orario d'inizio " + dto.getOraInizio() + " dev'essere precedente all'orario di fine " + oraFine);
		}

		List<Spettacolo> spettacoliEsistenti = repository.findSpettacoliInSalaPerData(dto.getIdSala(), dto.getData());
		for (Spettacolo s : spettacoliEsistenti) {
			boolean sovrapposto = dto.getOraInizio().isBefore(s.getOraFine()) &&
					s.getOraInizio().isBefore(oraFine);

			if (sovrapposto) {
				throw new MEConflictException("la sala è già occupata in questa fascia oraria: " + dto.getOraInizio() + " - " + oraFine);
			}
		}

		Spettacolo spettacolo = mapper.fromInsert(dto, sala, film, oraFine);

		spettacolo.setPostiRimanenti(repositoryP.getNumeriPostiSala(sala.getId()));

		return mapper.toResponse(repository.save(spettacolo));
	}

	@Override
	public List<ResponseSpettacoloDTO> findAll() {
		List<Spettacolo> spettacoli = repository.findAll();
		if (spettacoli.isEmpty()) {
			throw new MENoContentException("la lista di spettacoli è vuota");
		}
		return spettacoli.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public ResponseSpettacoloDTO findById(Long id) {
		Spettacolo s = repository.findById(id).orElseThrow(() -> new MENotFoundException("spettacolo non trovato per id: " + id));
		return mapper.toResponse(s);
	}

	@Override
	public List<ResponseSpettacoloDTO> findByData(LocalDate data) {
		List<Spettacolo> spettacoli = repository.findByDataOrderByOraInizioAsc(data);
		if (spettacoli.isEmpty()) {
			throw new MENoContentException("la lista di spettacoli è vuota per la data: " + data);
		}
		return spettacoli.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ResponseSpettacoloDTO> findByIdFilm(Long id) {
		List<Spettacolo> spettacoli = repository.findByIdFilm(id);
	
		return spettacoli.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public ResponseSpettacoloDTO editById(Long id, EditSpettacoloDTO sMod) {
		Spettacolo spettacolo = repository.findById(id)
				.orElseThrow(() -> new MENotFoundException("spettacolo non trovato per id: " + id));

		if (sMod.getData() != null) spettacolo.setData(sMod.getData());
		if (sMod.getOraInizio() != null) spettacolo.setOraInizio(sMod.getOraInizio());
		if (sMod.getOraFine() != null) spettacolo.setOraFine(sMod.getOraFine());
		if (sMod.getPostiRimanenti() != null) spettacolo.setPostiRimanenti(sMod.getPostiRimanenti());

		if (sMod.getIdSala() != null) spettacolo.setSala(repositoryS.findById(sMod.getIdSala())
				.orElseThrow(() -> new MENotFoundException("sala non trovata per id: " + sMod.getIdSala())));
		if (sMod.getIdFilm() != null) spettacolo.setFilm(repositoryF.findById(sMod.getIdFilm())
				.orElseThrow(() -> new MENotFoundException("film non trovato per id: " + sMod.getIdFilm())));

		if (sMod.getIdBiglietti() != null) {
			List<Biglietto> biglietti = repositoryB.findAllById(sMod.getIdBiglietti());
			if (biglietti.size() != sMod.getIdBiglietti().size()) {
				throw new MENotFoundException("uno o più biglietti non sono stati trovati per gli id forniti");
			}
			spettacolo.setBiglietti(biglietti);
		}

		for (Spettacolo s : repository.findSpettacoliInSalaPerData(spettacolo.getSala().getId(), spettacolo.getData())) {
			if (s.getId().equals(spettacolo.getId())) continue;

			boolean sovrapposto = spettacolo.getOraInizio().isBefore(s.getOraFine()) &&
					s.getOraInizio().isBefore(spettacolo.getOraFine());

			if (sovrapposto) {
				throw new MEConflictException("la sala è già occupata in questa fascia oraria: " + spettacolo.getOraInizio() + " - " + spettacolo.getOraFine());
			}
		}

		return mapper.toResponse(repository.save(spettacolo));
	}

	@Override
	public void removeById(Long id) {
		Spettacolo daRimuovere = repository.findById(id).orElseThrow(() -> new MENotFoundException("spettacolo non trovato per id: " + id));
		Optional<List<Biglietto>> optional = repositoryB.findByIdSpettacolo(daRimuovere.getId());
		if (optional.isPresent()) {
			List<Biglietto> biglietti = optional.get();
			repositoryB.deleteAll(biglietti);
		}
		repository.deleteById(id);
	}

	@Override
	public Map<Long, BigDecimal> getFatturatoSpettacoli() {
		List<Spettacolo> spettacoli = repository.findAll();
		return spettacoli.stream().collect(Collectors.toMap(
			Spettacolo::getId,
			s -> s.getBiglietti() == null ? java.math.BigDecimal.ZERO :
				s.getBiglietti().stream()
					.map(Biglietto::getPrezzo)
					.reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
		));
	}
}
