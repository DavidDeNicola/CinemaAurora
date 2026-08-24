package org.elis.movieexplorer.mapper;

import org.elis.movieexplorer.dto.spettacolo.request.EditSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.request.InsertListSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.request.InsertSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.response.ResponseSpettacoloDTO;
import org.elis.movieexplorer.model.Biglietto;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.Spettacolo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpettacoloMapper {
	public Spettacolo fromInsert(InsertSpettacoloDTO dto, Sala sala, Film film, LocalDateTime oraFine) {
		Spettacolo s = new Spettacolo();
		s.setData(dto.getData());
		s.setOraInizio(dto.getOraInizio());
		s.setOraFine(oraFine);
		s.setPostiRimanenti(sala.getPosti().size());
		s.setSala(sala);
		s.setFilm(film);
		return s;
	}

	public List<Spettacolo> fromInsertList(InsertListSpettacoloDTO dto, Sala sala, Film film) {
		List<Spettacolo> spettacoli = new ArrayList<>();
		HashMap<LocalDateTime, LocalDateTime> orariInizioFine = dto.getOrariInizioFine();

		for (LocalDate data : dto.getDate()) {
			for (LocalDateTime oraInizio : orariInizioFine.keySet()) {
				Spettacolo s = new Spettacolo();
				s.setData(data);
				s.setOraInizio(oraInizio);
				s.setOraFine(orariInizioFine.get(oraInizio));
				s.setPostiRimanenti(sala.getPosti().size());
				s.setSala(sala);
				s.setFilm(film);

				spettacoli.add(s);
			}
		}

		return spettacoli;
	}

	public Spettacolo fromEdit(EditSpettacoloDTO dto, Sala sala, Film film, List<Biglietto> biglietti) {
		Spettacolo s = new Spettacolo();
		s.setData(dto.getData());
		s.setOraInizio(dto.getOraInizio());
		s.setOraFine(dto.getOraFine());
		s.setPostiRimanenti(dto.getPostiRimanenti());
		s.setBiglietti(biglietti);
		s.setSala(sala);
		s.setFilm(film);
		return s;
	}

	public ResponseSpettacoloDTO toResponse(Spettacolo s) {
		ResponseSpettacoloDTO dto = new ResponseSpettacoloDTO();
		dto.setId(s.getId());
		dto.setData(s.getData());
		dto.setOraInizio(s.getOraInizio());
		dto.setOraFine(s.getOraFine());
		dto.setPostiRimanenti(s.getPostiRimanenti());
		dto.setIdBiglietti(
			    s.getBiglietti() == null ? new ArrayList<>() :
			    s.getBiglietti().stream()
			        .map(Biglietto::getId)
			        .collect(Collectors.toList())
			);
		dto.setNomeSala(s.getSala().getNome());
		dto.setTipoSala(s.getSala().getTipo().name().toUpperCase());
		dto.setNomeFilm(s.getFilm().getTitolo());
		dto.setIdFilm(s.getFilm().getId());
		return dto;
	}
}
