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
		Optional<Utente> optional = utenteRepository.findUtenteByEmail("admin@gmail.com");
		if(optional.isPresent()) {
			System.out.println("super già presente");
			return;
		}
		Utente utente = new Utente();
		utente.setNome("admin");
		utente.setCognome("admin");
		utente.setEmail("admin@gmail.com");
		utente.setPassword(passwordEncoder.encode("Password1!"));
		utente.setRuolo(Ruolo.SUPERADMIN);
		utente.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente);
		
		Utente utente2 = new Utente();
		utente2.setNome("Luca");
		utente2.setCognome("Bianchi");
		utente2.setEmail("luca.bianchi@movieexplorer.it");
		utente2.setPassword(passwordEncoder.encode("Password1!"));
		utente2.setRuolo(Ruolo.STAFF);
		utente2.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente2);
		
		Utente utente3 = new Utente();
		utente3.setNome("Giulia");
		utente3.setCognome("Verdi");
		utente3.setEmail("giulia.verdi@movieexplorer.it");
		utente3.setPassword(passwordEncoder.encode("Password1!"));
		utente3.setRuolo(Ruolo.STAFF);
		utente3.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente3);
		
		Utente utente4 = new Utente();
		utente4.setNome("Marco");
		utente4.setCognome("Neri");
		utente4.setEmail("marco.neri@gmail.com");
		utente4.setPassword(passwordEncoder.encode("Password1!"));
		utente4.setRuolo(Ruolo.CLIENTE);
		utente4.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente4);
		
		Utente utente5 = new Utente();
		utente5.setNome("Anna");
		utente5.setCognome("Ferrari");
		utente5.setEmail("anna.ferrari@gmail.com");
		utente5.setPassword(passwordEncoder.encode("Password1!"));
		utente5.setRuolo(Ruolo.CLIENTE);
		utente5.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente5);
		
		Utente utente6 = new Utente();
		utente6.setNome("Paolo");
		utente6.setCognome("Romano");
		utente6.setEmail("paolo.romano@email.it");
		utente6.setPassword(passwordEncoder.encode("Password1!"));
		utente6.setRuolo(Ruolo.CLIENTE);
		utente6.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente6);
		
		Utente utente7 = new Utente();
		utente7.setNome("Sara");
		utente7.setCognome("Colombo");
		utente7.setEmail("sara.colombo@email.it");
		utente7.setPassword(passwordEncoder.encode("Password1!"));
		utente7.setRuolo(Ruolo.CLIENTE);
		utente7.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente7);
		
		Utente utente8 = new Utente();
		utente8.setNome("Federico");
		utente8.setCognome("Ricci");
		utente8.setEmail("federico.ricci@email.it");
		utente8.setPassword(passwordEncoder.encode("Password1!"));
		utente8.setRuolo(Ruolo.CLIENTE);
		utente8.setBiglietti(new ArrayList<>());
		utenteRepository.save(utente8);
		
		
		
	}
	

}
