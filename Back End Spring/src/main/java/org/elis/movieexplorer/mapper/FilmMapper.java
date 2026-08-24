package org.elis.movieexplorer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.elis.movieexplorer.dto.film.request.EditFilmDTO;
import org.elis.movieexplorer.dto.film.request.InsertFilmDTO;
import org.elis.movieexplorer.dto.film.response.ResponseFilmDTO;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Genere;
import org.elis.movieexplorer.model.Spettacolo;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

	public Film fromInsert(InsertFilmDTO dto, List<Genere> generi) {
		Film f = generateFilm(generi, dto.getImdbID(), dto.getTitolo(), dto.getDescrizione(), dto.getDurata(), dto.getAttori(), dto.getUrlLocandina());
		f.setUrlTrailer(dto.getUrlTrailer());
		return f;
	}

	public Film fromEdit(EditFilmDTO dto, List<Genere> generi, List<Spettacolo> spettacoli) {
		Film f = generateFilm(generi, null, dto.getTitolo(), dto.getDescrizione(), dto.getDurata(), dto.getAttori(), dto.getUrlLocandina());
		f.setUrlTrailer(dto.getUrlTrailer());
		f.setSpettacoli(spettacoli);
		return f;
	}

	public ResponseFilmDTO toResponse(Film f) {
		ResponseFilmDTO dto = new ResponseFilmDTO();
		dto.setId(f.getId());
		dto.setTitolo(f.getTitolo());
		dto.setDescrizione(f.getDescrizione());
		dto.setDurata(f.getDurata());
		dto.setAttori(f.getAttori());
		dto.setUrlLocandina(f.getUrlLocandina());
		dto.setUrlTrailer(f.getUrlTrailer());
		dto.setNomeGeneri(f.getGeneri().stream().map(g -> g.getNome()).collect(Collectors.toList()));
		return dto;
	}

	private Film generateFilm(List<Genere> generi, String imdbId, String titolo, String descrizione, Integer durata, String cast, String urlLocandina) {
		Film f = new Film();
		f.setImdbID(imdbId);
		f.setTitolo(titolo);
		f.setDescrizione(descrizione);
		f.setDurata(durata);
		f.setAttori(cast);
		f.setUrlLocandina(urlLocandina);
		f.setGeneri(generi);
		return f;
	}
}
