package org.elis.movieexplorer.service.jpa;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.dto.biglietto.request.InsertBigliettoDTO;
import org.elis.movieexplorer.dto.biglietto.response.ResponseBigliettoDTO;
import org.elis.movieexplorer.exception.definition.MENoContentException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.exception.definition.MEPastTicketDeletionException;
import org.elis.movieexplorer.exception.definition.MEUnprocessableEntityException;
import org.elis.movieexplorer.mapper.BigliettoMapper;
import org.elis.movieexplorer.model.Biglietto;
import org.elis.movieexplorer.model.Spettacolo;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.Posto;
import org.elis.movieexplorer.repository.BigliettoRepository;
import org.elis.movieexplorer.repository.PostoRepository;
import org.elis.movieexplorer.repository.SpettacoloRepository;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.elis.movieexplorer.service.definition.BigliettoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name="service.impl", havingValue="JPA")
@RequiredArgsConstructor
public class BigliettoServiceJPA implements BigliettoService{

	private final BigliettoRepository repositoryB;
	private final UtenteRepository repositoryU;
	private final SpettacoloRepository repositoryS;
	private final PostoRepository repositoryP;
	private final BigliettoMapper mapperB;
	
	@Override
	@Transactional
	public List<ResponseBigliettoDTO> insert(InsertBigliettoDTO dto, Utente utenteLoggato) {
		
		
		Optional<Spettacolo> oSpettacolo = repositoryS.findById(dto.getIdSpettacolo());
		Spettacolo spettacolo = oSpettacolo.orElseThrow(() -> new MENotFoundException("nessuno spettacolo trovato"));
		
		int postiAttuali = spettacolo.getPostiRimanenti();
	    List<Integer> richiesti = dto.getIdPosti();

		if(postiAttuali <= 0) throw new MEUnprocessableEntityException("posti esauriti");
		if(richiesti.size() > postiAttuali) throw new MEUnprocessableEntityException("disponibilità biglietti insufficente");
		
		List<Posto> posti = richiesti.stream()
				.map(i -> repositoryP.findById((long) i).orElseThrow(() -> new MENotFoundException("Posto non trovato per id: "+i)))
				.collect(Collectors.toList());
		
		List<Biglietto> bList = mapperB.fromInsert(posti, spettacolo, utenteLoggato);
		for(Biglietto b : bList) {
			b.setPrezzo(spettacolo.getSala().getTipo().getPrezzo());
			String codice = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
			b.setCodiceBiglietto(codice);
		}
			
		List<Biglietto> salvati = repositoryB.saveAll(bList);
		
		List<ResponseBigliettoDTO> risultato = new ArrayList<>();
		for (Biglietto b : salvati) {
		    ResponseBigliettoDTO dTO = mapperB.toResponse(b);
		    risultato.add(dTO);
		}
		return risultato;
	}

	@Override
	public List<ResponseBigliettoDTO> findByIdUtente(Long idUtente) {
		List<ResponseBigliettoDTO> daRitornare = repositoryB.findByIdUtente(idUtente).stream().map(b -> mapperB.toResponse(b)).collect(Collectors.toList());
		if(daRitornare.isEmpty()) throw new MENoContentException("lista film vuota");
		return daRitornare;
	}

	@Override
	public List<ResponseBigliettoDTO> findByIdSpettacolo(Long idSpettacolo) {
		Optional<List<Biglietto>> bigliettiOpt =  repositoryB.findByIdSpettacolo(idSpettacolo);
		List<Biglietto> biglietti = bigliettiOpt
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Nessun biglietto trovato"));
		if(biglietti.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nessun biglietto trovato");
		return biglietti.stream().map(b -> mapperB.toResponse(b)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void removeByIdEmail(Long idBiglietto, String email) {
		Biglietto biglietto = repositoryB.findById(idBiglietto).orElseThrow(() -> new MENotFoundException("biglietto non trovato"));
		Utente utente = repositoryU.findUtenteByEmail(email).orElseThrow(() -> new MENotFoundException("utente non trovato"));
		if(!utente.getBiglietti().contains(biglietto)) 
			throw new MENotFoundException("biglietto non trovato tra i biglietti dell'utente");
		LocalDateTime oraLimiteCancellazione = biglietto.getSpettacolo().getOraInizio().minusHours(1);
		if(LocalDateTime.now().isAfter(oraLimiteCancellazione))
			throw new MEPastTicketDeletionException("non è più possibile cancellare il biglietto a meno di un'ora dall'inizio dello spettacolo");
		Spettacolo spettacolo = biglietto.getSpettacolo();
		    
		repositoryS.save(spettacolo);
		repositoryB.deleteById(idBiglietto);
	}

}
