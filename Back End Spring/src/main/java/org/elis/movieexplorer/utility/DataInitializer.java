package org.elis.movieexplorer.utility;

import java.util.ArrayList;
import java.util.Optional;

import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.enums.Ruolo;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final UtenteRepository utenteRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		creaUtenteSeNonEsiste("admin", "admin", "admin@gmail.com", Ruolo.SUPERADMIN);
		creaUtenteSeNonEsiste("Luca", "Bianchi", "luca.bianchi@movieexplorer.it", Ruolo.STAFF);
		creaUtenteSeNonEsiste("Giulia", "Verdi", "giulia.verdi@movieexplorer.it", Ruolo.STAFF);
		creaUtenteSeNonEsiste("Marco", "Neri", "marco.neri@gmail.com", Ruolo.CLIENTE);
		creaUtenteSeNonEsiste("Anna", "Ferrari", "anna.ferrari@gmail.com", Ruolo.CLIENTE);
		creaUtenteSeNonEsiste("Paolo", "Romano", "paolo.romano@email.it", Ruolo.CLIENTE);
		creaUtenteSeNonEsiste("Sara", "Colombo", "sara.colombo@email.it", Ruolo.CLIENTE);
		creaUtenteSeNonEsiste("Federico", "Ricci", "federico.ricci@email.it", Ruolo.CLIENTE);
	}

	private void creaUtenteSeNonEsiste(String nome, String cognome, String email, Ruolo ruolo) {
		Optional<Utente> optional = utenteRepository.findUtenteByEmail(email);
		if (optional.isPresent()) {
			System.out.println(email + " già presente");
			return;
		}

		Utente utente = new Utente();
		utente.setNome(nome);
		utente.setCognome(cognome);
		utente.setEmail(email);
		utente.setPassword(passwordEncoder.encode("Password1!"));
		utente.setRuolo(ruolo);
		utente.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente);
	}
}