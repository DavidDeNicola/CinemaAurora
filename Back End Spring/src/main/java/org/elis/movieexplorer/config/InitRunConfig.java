package org.elis.movieexplorer.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.elis.movieexplorer.model.Biglietto;
import org.elis.movieexplorer.model.Chat;
import org.elis.movieexplorer.model.Film;
import org.elis.movieexplorer.model.Genere;
import org.elis.movieexplorer.model.Messaggio;
import org.elis.movieexplorer.model.Posto;
import org.elis.movieexplorer.model.Sala;
import org.elis.movieexplorer.model.Spettacolo;
import org.elis.movieexplorer.model.Token;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.enums.Ruolo;
import org.elis.movieexplorer.model.enums.StatoChat;
import org.elis.movieexplorer.model.enums.Tipo;
import org.elis.movieexplorer.repository.BigliettoRepository;
import org.elis.movieexplorer.repository.ChatRepository;
import org.elis.movieexplorer.repository.FilmRepository;
import org.elis.movieexplorer.repository.GenereRepository;
import org.elis.movieexplorer.repository.MessaggioRepository;
import org.elis.movieexplorer.repository.PostoRepository;
import org.elis.movieexplorer.repository.SalaRepository;
import org.elis.movieexplorer.repository.SpettacoloRepository;
import org.elis.movieexplorer.repository.TokenRepository;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitRunConfig implements CommandLineRunner {

	private final PasswordEncoder passwordEncoder;
	private final PostoRepository postoRepository;
	private final SalaRepository salaRepository;
	private final UtenteRepository utenteRepository;
	private final GenereRepository genereRepository;
	private final FilmRepository filmRepository;
	private final SpettacoloRepository spettacoloRepository;
	private final BigliettoRepository bigliettoRepository;
	private final ChatRepository chatRepository;
	private final MessaggioRepository messaggioRepository;
	private final TokenRepository tokenRepository;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("🔄 Avvio popolamento assistito del Database...");

		// ==========================================
		// 1. POPOLAMENTO UTENTI
		// ==========================================
		// — utenti originali del collega —
		Utente admin  = null, staff1 = null, staff2 = null;
		Utente user1  = null, user2  = null, user3  = null;
		Utente user4  = null, user5  = null, user6  = null, user7 = null, user8 = null;
		// — utenti aggiuntivi —
		Utente staff3 = null;
		Utente user9  = null, user10 = null, user11 = null, user12 = null, user13 = null;

		try {
			Optional<Utente> adminOpt = utenteRepository.findUtenteByEmail("admin@gmail.com");
			Optional<Utente> staffOpt = utenteRepository.findUtenteByEmail("staff@gmail.com");
			Optional<Utente> userOpt  = utenteRepository.findUtenteByEmail("user@gmail.com");

			// — originali —
			admin = adminOpt.orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.SUPERADMIN, "Admin", "System", "admin@gmail.com",
					passwordEncoder.encode("Admin123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			staff1 = staffOpt.orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.STAFF, "Mario", "Rossi", "staff@gmail.com",
					passwordEncoder.encode("Staff123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user1 = userOpt.orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Giulia", "Bianchi", "user@gmail.com",
					passwordEncoder.encode("User123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			staff2 = utenteRepository.findUtenteByEmail("anna.neri@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.STAFF, "Anna", "Neri", "anna.neri@gmail.com",
					passwordEncoder.encode("Staff456!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user2 = utenteRepository.findUtenteByEmail("marco.rossi@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Marco", "Rossi", "marco.rossi@gmail.com",
					passwordEncoder.encode("Marco123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user3 = utenteRepository.findUtenteByEmail("elena.verdi@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Elena", "Verdi", "elena.verdi@gmail.com",
					passwordEncoder.encode("Elena123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user4 = utenteRepository.findUtenteByEmail("giulia.bianchi@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Giulia", "Bianchi", "giulia.bianchi@gmail.com",
					passwordEncoder.encode("Giulia123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user5 = utenteRepository.findUtenteByEmail("marco.esposito@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Marco", "Esposito", "marco.esposito@gmail.com",
					passwordEncoder.encode("Marco123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			// user6 punta alla stessa email di user3 (elena.verdi@gmail.com) — comportamento originale preservato
			user6 = utenteRepository.findUtenteByEmail("elena.verdi@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Elena", "Verdi", "elena.verdi@gmail.com",
					passwordEncoder.encode("Elena123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user7 = utenteRepository.findUtenteByEmail("davide.conti@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Davide", "Conti", "davide.conti@gmail.com",
					passwordEncoder.encode("Davide123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user8 = utenteRepository.findUtenteByEmail("sara.marino@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Sara", "Marino", "sara.marino@gmail.com",
					passwordEncoder.encode("Sara123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			// — nuovi utenti aggiuntivi —
			staff3 = utenteRepository.findUtenteByEmail("luca.ferrari@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.STAFF, "Luca", "Ferrari", "luca.ferrari@gmail.com",
					passwordEncoder.encode("Staff789!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user9 = utenteRepository.findUtenteByEmail("sofia.ricci@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Sofia", "Ricci", "sofia.ricci@gmail.com",
					passwordEncoder.encode("Sofia123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user10 = utenteRepository.findUtenteByEmail("andrea.colombo@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Andrea", "Colombo", "andrea.colombo@gmail.com",
					passwordEncoder.encode("Andrea123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user11 = utenteRepository.findUtenteByEmail("valentina.greco@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Valentina", "Greco", "valentina.greco@gmail.com",
					passwordEncoder.encode("Vale123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user12 = utenteRepository.findUtenteByEmail("francesco.bruno@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Francesco", "Bruno", "francesco.bruno@gmail.com",
					passwordEncoder.encode("Fran123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

			user13 = utenteRepository.findUtenteByEmail("chiara.fontana@gmail.com").orElseGet(() -> utenteRepository.save(
				new Utente(null, Ruolo.CLIENTE, "Chiara", "Fontana", "chiara.fontana@gmail.com",
					passwordEncoder.encode("Chiara123!"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
			));

		} catch (Exception e) {
			System.err.println("❌ Errore durante il popolamento degli utenti: " + e.getMessage());
		}

		// ==========================================
		// 2. SALE E POSTI
		// ==========================================
		List<Sala> sale = new ArrayList<>();
		List<Posto> tuttiIPosti = new ArrayList<>();
		try {
			sale = salaRepository.findAll();
			if (sale.isEmpty()) {
				// — sale originali del collega —
				List<Sala> saleDaInserire = new ArrayList<>(List.of(
					new Sala(null, "Sala IMAX Centrale",  Tipo.IMAX,   new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala 3D Galaxy",       Tipo.TRED,   new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala Rossa",           Tipo.NORMALE, new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala Premium IMAX",    Tipo.IMAX,   new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala 3D Experience",   Tipo.TRED,   new ArrayList<>(), new ArrayList<>()),
					// — sale aggiuntive —
					new Sala(null, "Sala Blu",             Tipo.NORMALE, new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala Verde",           Tipo.NORMALE, new ArrayList<>(), new ArrayList<>()),
					new Sala(null, "Sala IMAX Platino",    Tipo.IMAX,   new ArrayList<>(), new ArrayList<>())
				));
				sale = salaRepository.saveAll(saleDaInserire);

				List<Posto> postiDaInserire = new ArrayList<>();
				for (Sala salaItem : sale) {
					// IMAX → 6 file × 12 colonne, 3D → 5 × 10, NORMALE → 4 × 8
					int filaMax   = salaItem.getTipo() == Tipo.IMAX   ? 6
					              : salaItem.getTipo() == Tipo.TRED   ? 5 : 4;
					int colonnaMax = salaItem.getTipo() == Tipo.IMAX  ? 12
					              : salaItem.getTipo() == Tipo.TRED   ? 10 : 8;
					for (int fila = 1; fila <= filaMax; fila++) {
						for (int colonna = 1; colonna <= colonnaMax; colonna++) {
							postiDaInserire.add(new Posto(null, fila, colonna, salaItem));
						}
					}
				}
				tuttiIPosti = postoRepository.saveAll(postiDaInserire);
			} else {
				tuttiIPosti = postoRepository.findAll();
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante il popolamento di sale/posti: " + e.getMessage());
		}

		// ==========================================
		// 3. GENERI
		// ==========================================
		List<Genere> generi = new ArrayList<>();
		try {
			generi = genereRepository.findAll();
			if (generi.isEmpty()) {
				generi = genereRepository.saveAll(List.of(
					// — originali —
					new Genere(null, "Azione",       new ArrayList<>()),
					new Genere(null, "Fantascienza",  new ArrayList<>()),
					new Genere(null, "Drammatico",    new ArrayList<>()),
					new Genere(null, "Thriller",      new ArrayList<>()),
					new Genere(null, "Animazione",    new ArrayList<>()),
					new Genere(null, "Commedia",      new ArrayList<>()),
					// — aggiuntivi —
					new Genere(null, "Horror",        new ArrayList<>()),
					new Genere(null, "Avventura",     new ArrayList<>()),
					new Genere(null, "Biografico",    new ArrayList<>()),
					new Genere(null, "Crime",         new ArrayList<>()),
					new Genere(null, "Romantico",     new ArrayList<>()),
					new Genere(null, "Storico",       new ArrayList<>())
				));
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante il popolamento dei generi: " + e.getMessage());
		}

		// ==========================================
		// 4. FILM
		// ==========================================
		List<Film> filmInCatalogo = new ArrayList<>();
		try {
			filmInCatalogo = filmRepository.findAll();
			if (filmInCatalogo.isEmpty() && !generi.isEmpty()) {

				Genere az = g(generi, "Azione");
				Genere fa = g(generi, "Fantascienza");
				Genere dr = g(generi, "Drammatico");
				Genere th = g(generi, "Thriller");
				Genere an = g(generi, "Animazione");
				Genere co = g(generi, "Commedia");
				Genere ho = g(generi, "Horror");
				Genere av = g(generi, "Avventura");
				Genere bi = g(generi, "Biografico");
				Genere cr = g(generi, "Crime");
				Genere ro = g(generi, "Romantico");
				Genere st = g(generi, "Storico");

				// — film originali del collega (con IMDB ID corretti) —
				Film f1 = new Film(null, "tt1745960",
					"Top Gun: Maverick",
					"Dopo più di trent'anni di servizio, Pete 'Maverick' Mitchell è ancora ai margini della Marina come collaudatore, evitando un avanzamento di grado che lo metterebbe a terra.",
					130, "Tom Cruise, Miles Teller, Jennifer Connelly, Jon Hamm",
					"https://m.media-amazon.com/images/M/MV5BZWYzOGEwNTgtNWU3NS00ZTQ0LWJkODUtMmVhMjIwMjA1ZmQwXkEyXkFqcGdeQXVyMjkwOTAyMDU@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=qSqVVswa420",
					List.of(az, dr), new ArrayList<>());

				Film f2 = new Film(null, "tt0816692",
					"Interstellar",
					"Un gruppo di esploratori usa un wormhole appena scoperto per superare i limiti dei viaggi spaziali umani e conquistare le vaste distanze dell'universo.",
					169, "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine",
					"https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWgwMjgtZTIzNWQxZGRiNDg0XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=zSWdZVtXT7E",
					List.of(fa, dr, av), new ArrayList<>());

				Film f3 = new Film(null, "tt1375666",
					"Inception",
					"Un ladro che ruba segreti corporativi attraverso la tecnologia di condivisione dei sogni riceve il compito inverso di piantare un'idea nella mente di un CEO.",
					148, "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Ken Watanabe",
					"https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=YoHD9XEInc0",
					List.of(fa, az, th), new ArrayList<>());

				// — film aggiuntivi —
				Film f4 = new Film(null, "tt15398776",
					"Oppenheimer",
					"La storia di J. Robert Oppenheimer e il suo ruolo nello sviluppo della bomba atomica durante il Progetto Manhattan nella Seconda Guerra Mondiale.",
					180, "Cillian Murphy, Emily Blunt, Matt Damon, Robert Downey Jr.",
					"https://m.media-amazon.com/images/M/MV5BMDBmYTZjNjUtN2M1MS00MTQ2LTk2ODgtNzc2M2QyZGE5NTVjXkEyXkFqcGdeQXVyNzAwMjU2MTY@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=uYPbbksJxIg",
					List.of(bi, dr, st), new ArrayList<>());

				Film f5 = new Film(null, "tt9362722",
					"Spider-Man: No Way Home",
					"Con l'identità di Spider-Man ormai rivelata, Peter Parker chiede a Doctor Strange di far dimenticare a tutti il suo segreto. Un incantesimo che va storto apre il multiverso.",
					148, "Tom Holland, Zendaya, Benedict Cumberbatch, Alfred Molina",
					"https://m.media-amazon.com/images/M/MV5BZWMyYzFjYTYtNTRjYi00OGExLWE2YzgtOGRmYjAxZTU3NzBiXkEyXkFqcGdeQXVyMzQ0MzA0NTM@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=JfVOs4VSpmA",
					List.of(az, av, fa), new ArrayList<>());

				Film f6 = new Film(null, "tt1160419",
					"Dune: Parte Uno",
					"Paul Atreides, giovane brillante e talentuoso nato in un destino al di là della sua comprensione, deve recarsi al pianeta più pericoloso dell'universo per assicurare il futuro della sua famiglia.",
					155, "Timothée Chalamet, Zendaya, Oscar Isaac, Rebecca Ferguson",
					"https://m.media-amazon.com/images/M/MV5BN2FjNmEyNWMtYzM0ZS00NjIyLTg5YzYtYThlMGVjNzE1OGViXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=8g18jFHCLXk",
					List.of(fa, av, dr), new ArrayList<>());

				Film f7 = new Film(null, "tt4154796",
					"Avengers: Endgame",
					"Dopo gli eventi devastanti di Infinity War l'universo è in rovina. I Vendicatori si radunano per annullare le azioni di Thanos e ripristinare l'ordine nell'universo per sempre.",
					181, "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth, Scarlett Johansson",
					"https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=TcMBFSGVi1c",
					List.of(az, av, fa), new ArrayList<>());

				Film f8 = new Film(null, "tt6751668",
					"Parasite",
					"L'intera famiglia Ki-taek è disoccupata e vive in un seminterrato. Quando il figlio ottiene lavoro come insegnante di inglese per una famiglia ricca, inizia una serie di eventi inaspettati.",
					132, "Song Kang-ho, Lee Sun-kyun, Cho Yeo-jeong, Choi Woo-shik",
					"https://m.media-amazon.com/images/M/MV5BYWZjMjk3ZTItODQ2ZC00NTY5LWE0ZDYtZTI3MjcwN2Q5NTVkXkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=5xH0HfJHsaY",
					List.of(dr, th, cr), new ArrayList<>());

				Film f9 = new Film(null, "tt7286456",
					"Joker",
					"Arthur Fleck, un comico fallito, porta al crimine e al caos la corrotta Gotham City. La storia mostra la sua trasformazione nel celebre criminale della DC Comics.",
					122, "Joaquin Phoenix, Robert De Niro, Zazie Beetz, Frances Conroy",
					"https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDCtN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=zAGVQLHvwOY",
					List.of(cr, dr, th), new ArrayList<>());

				Film f10 = new Film(null, "tt0468569",
					"Il Cavaliere Oscuro",
					"Quando il Joker emerge dalle sue tenebre per creare il caos a Gotham, Batman deve confrontarsi con una delle più grandi prove psicologiche della sua capacità di combattere l'ingiustizia.",
					152, "Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine",
					"https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=EXeTwQWrcwY",
					List.of(az, cr, dr), new ArrayList<>());

				Film f11 = new Film(null, "tt2106476",
					"The Revenant - Redivivo",
					"Un esploratore di pellicce degli anni '20 dell'Ottocento cerca vendetta contro i cacciatori che lo hanno lasciato per morto dopo un violento attacco di un orso grizzly.",
					156, "Leonardo DiCaprio, Tom Hardy, Will Poulter, Domhnall Gleeson",
					"https://m.media-amazon.com/images/M/MV5BMDE5OWViOWQtNjU2ZS00ZWVkLThiZjItMDg3ZmYwNWI4ZjM2XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=NN0f30Vf80c",
					List.of(dr, av), new ArrayList<>());

				Film f12 = new Film(null, "tt1843866",
					"Captain America: The Winter Soldier",
					"Steve Rogers combatte fianco a fianco con Natasha Romanoff e il nuovo alleato Sam Wilson per svelare una cospirazione mentre affronta il pericoloso Winter Soldier.",
					136, "Chris Evans, Scarlett Johansson, Anthony Mackie, Sebastian Stan",
					"https://m.media-amazon.com/images/M/MV5BMzA2NDkwODAwM15BMl5BanBnXkFtZTgwODk5MTgwMTE@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=7SlILk2WMTI",
					List.of(az, av, th), new ArrayList<>());

				Film f13 = new Film(null, "tt0903624",
					"Lo Hobbit: Un Viaggio Inaspettato",
					"Lo Hobbit Bilbo Baggins viene trascinato in una straordinaria avventura da Gandalf il Grigio e una compagnia di tredici nani che vogliono riconquistare la loro patria.",
					169, "Martin Freeman, Ian McKellen, Richard Armitage, Cate Blanchett",
					"https://m.media-amazon.com/images/M/MV5BMTcwNTE4MTUxMl5BMl5BanBnXkFtZTcwMDIyODM4OA@@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=SDnYMbYB-nU",
					List.of(av, fa), new ArrayList<>());

				Film f14 = new Film(null, "tt0110912",
					"Pulp Fiction",
					"Le vite di due sicari della mafia, un pugile, della moglie di un gangster e di una coppia di rapinatori si intrecciano in quattro storie di violenza e redenzione.",
					154, "John Travolta, Uma Thurman, Samuel L. Jackson, Bruce Willis",
					"https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=s7EdQ4FqbhY",
					List.of(cr, th), new ArrayList<>());

				Film f15 = new Film(null, "tt0137523",
					"Fight Club",
					"Un impiegato insoddisfatto e un venditore di sapone caotico formano un club di lotta clandestino che evolve in qualcosa di molto più pericoloso.",
					139, "Brad Pitt, Edward Norton, Helena Bonham Carter",
					"https://m.media-amazon.com/images/M/MV5BNDIzNDU0YzEtYzE5Ni00ZjlkLTk5ZjgtNjM3NWE4YzA3Nzk3XkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_SX300.jpg",
					"https://www.youtube.com/watch?v=qtRKdVHc-cE",
					List.of(dr, th), new ArrayList<>());

				filmInCatalogo = filmRepository.saveAll(List.of(
					f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15
				));
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante il popolamento dei film: " + e.getMessage());
		}

		// ==========================================
		// 5. SPETTACOLI
		// ==========================================
		List<Spettacolo> spettacoli = new ArrayList<>();
		try {
			spettacoli = spettacoloRepository.findAll();
			if (spettacoli.isEmpty() && !sale.isEmpty() && filmInCatalogo.size() >= 3) {

				LocalDate oggi      = LocalDate.now();
				LocalDate domani    = oggi.plusDays(1);
				LocalDate dopo      = oggi.plusDays(2);
				LocalDate tra3      = oggi.plusDays(3);
				LocalDate tra5      = oggi.plusDays(5);
				LocalDate tra7      = oggi.plusDays(7);

				Sala s1 = sale.get(0); // IMAX Centrale
				Sala s2 = sale.get(1); // 3D Galaxy
				Sala s3 = sale.get(2); // Sala Rossa (NORMALE)
				Sala s4 = sale.get(3); // Premium IMAX
				Sala s5 = sale.get(4); // 3D Experience
				// sale aggiuntive (esistono solo se la lista ne ha abbastanza)
				Sala s6 = sale.size() > 5 ? sale.get(5) : s3; // Sala Blu
				Sala s7 = sale.size() > 6 ? sale.get(6) : s3; // Sala Verde
				Sala s8 = sale.size() > 7 ? sale.get(7) : s1; // IMAX Platino

				// posti totali per sala (usati per inizializzare postiRimanenti)
				int pS1 = postiSala(tuttiIPosti, s1);
				int pS2 = postiSala(tuttiIPosti, s2);
				int pS3 = postiSala(tuttiIPosti, s3);
				int pS4 = postiSala(tuttiIPosti, s4);
				int pS5 = postiSala(tuttiIPosti, s5);
				int pS6 = postiSala(tuttiIPosti, s6);
				int pS7 = postiSala(tuttiIPosti, s7);
				int pS8 = postiSala(tuttiIPosti, s8);

				Film topGun      = filmInCatalogo.get(0);
				Film interstellar = filmInCatalogo.get(1);
				Film inception   = filmInCatalogo.get(2);
				Film oppenheimer = filmInCatalogo.size() > 3  ? filmInCatalogo.get(3)  : topGun;
				Film spiderman   = filmInCatalogo.size() > 4  ? filmInCatalogo.get(4)  : topGun;
				Film dune        = filmInCatalogo.size() > 5  ? filmInCatalogo.get(5)  : interstellar;
				Film avengers    = filmInCatalogo.size() > 6  ? filmInCatalogo.get(6)  : topGun;
				Film parasite    = filmInCatalogo.size() > 7  ? filmInCatalogo.get(7)  : inception;
				Film joker       = filmInCatalogo.size() > 8  ? filmInCatalogo.get(8)  : inception;
				Film darkKnight  = filmInCatalogo.size() > 9  ? filmInCatalogo.get(9)  : topGun;
				Film revenant    = filmInCatalogo.size() > 10 ? filmInCatalogo.get(10) : interstellar;
				Film captAmerica = filmInCatalogo.size() > 11 ? filmInCatalogo.get(11) : topGun;
				Film hobbit      = filmInCatalogo.size() > 12 ? filmInCatalogo.get(12) : interstellar;
				Film pulpFiction = filmInCatalogo.size() > 13 ? filmInCatalogo.get(13) : parasite;
				Film fightClub   = filmInCatalogo.size() > 14 ? filmInCatalogo.get(14) : joker;

				List<Spettacolo> spettacoliDaInserire = new ArrayList<>();

				// OGGI
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,10,0),  ldt(oggi,12,10), pS1, null, s1, topGun));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,14,30), ldt(oggi,17,19), pS2, null, s2, interstellar));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,17,0),  ldt(oggi,19,28), pS3, null, s3, inception));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,20,30), ldt(oggi,22,40), pS1, null, s1, topGun));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,20,0),  ldt(oggi,23,0),  pS4, null, s4, oppenheimer));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,19,0),  ldt(oggi,21,28), pS5, null, s5, spiderman));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,21,0),  ldt(oggi,23,35), pS6, null, s6, dune));
				spettacoliDaInserire.add(new Spettacolo(null, oggi, ldt(oggi,16,0),  ldt(oggi,18,1),  pS7, null, s7, joker));

				// DOMANI
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,11,0),  ldt(domani,14,1),  pS8, null, s8, avengers));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,15,0),  ldt(domani,17,12), pS2, null, s2, parasite));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,17,30), ldt(domani,20,6),  pS3, null, s3, revenant));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,20,30), ldt(domani,22,32), pS4, null, s4, darkKnight));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,18,0),  ldt(domani,20,16), pS5, null, s5, captAmerica));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,21,15), ldt(domani,23,25), pS6, null, s6, fightClub));
				spettacoliDaInserire.add(new Spettacolo(null, domani, ldt(domani,16,30), ldt(domani,19,9),  pS7, null, s7, hobbit));

				// DOPODOMANI
				spettacoliDaInserire.add(new Spettacolo(null, dopo, ldt(dopo,13,0),  ldt(dopo,15,10), pS1, null, s1, topGun));
				spettacoliDaInserire.add(new Spettacolo(null, dopo, ldt(dopo,16,0),  ldt(dopo,18,49), pS2, null, s2, interstellar));
				spettacoliDaInserire.add(new Spettacolo(null, dopo, ldt(dopo,20,0),  ldt(dopo,23,0),  pS4, null, s4, oppenheimer));
				spettacoliDaInserire.add(new Spettacolo(null, dopo, ldt(dopo,19,30), ldt(dopo,21,34), pS5, null, s5, pulpFiction));
				spettacoliDaInserire.add(new Spettacolo(null, dopo, ldt(dopo,21,0),  ldt(dopo,23,3),  pS3, null, s3, joker));

				// TRA 3 GIORNI
				spettacoliDaInserire.add(new Spettacolo(null, tra3, ldt(tra3,15,30), ldt(tra3,17,58), pS8, null, s8, dune));
				spettacoliDaInserire.add(new Spettacolo(null, tra3, ldt(tra3,18,0),  ldt(tra3,21,1),  pS1, null, s1, avengers));
				spettacoliDaInserire.add(new Spettacolo(null, tra3, ldt(tra3,20,45), ldt(tra3,22,47), pS6, null, s6, darkKnight));

				// TRA 5 GIORNI
				spettacoliDaInserire.add(new Spettacolo(null, tra5, ldt(tra5,17,0),  ldt(tra5,19,49), pS2, null, s2, inception));
				spettacoliDaInserire.add(new Spettacolo(null, tra5, ldt(tra5,20,0),  ldt(tra5,22,16), pS4, null, s4, captAmerica));
				spettacoliDaInserire.add(new Spettacolo(null, tra5, ldt(tra5,21,30), ldt(tra5,23,39), pS7, null, s7, fightClub));

				// TRA 7 GIORNI
				spettacoliDaInserire.add(new Spettacolo(null, tra7, ldt(tra7,15,0),  ldt(tra7,17,10), pS1, null, s1, topGun));
				spettacoliDaInserire.add(new Spettacolo(null, tra7, ldt(tra7,19,0),  ldt(tra7,21,49), pS2, null, s2, hobbit));
				spettacoliDaInserire.add(new Spettacolo(null, tra7, ldt(tra7,20,30), ldt(tra7,22,44), pS5, null, s5, pulpFiction));

				spettacoli = spettacoloRepository.saveAll(spettacoliDaInserire);
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante il popolamento degli spettacoli: " + e.getMessage());
		}

		// ==========================================
		// 6. BIGLIETTI
		// ==========================================
		try {
			if (bigliettoRepository.count() == 0 && !spettacoli.isEmpty()
					&& !tuttiIPosti.isEmpty()
					&& user1 != null && user2 != null && user3 != null) {

				// — biglietti originali del collega —
				Spettacolo sp1IMAX = spettacoli.get(0);
				Spettacolo sp23D   = spettacoli.get(1);

				Posto posto1 = filtraPosto(tuttiIPosti, sp1IMAX.getSala().getId(), 1, 1);
				Posto posto2 = filtraPosto(tuttiIPosti, sp1IMAX.getSala().getId(), 1, 2);
				Posto posto3 = filtraPosto(tuttiIPosti, sp23D.getSala().getId(),   2, 3);

				bigliettoRepository.save(new Biglietto(null, user1,
					UUID.randomUUID().toString().substring(0,8).toUpperCase(), BigDecimal.valueOf(12.50), sp1IMAX, posto1));
				bigliettoRepository.save(new Biglietto(null, user2,
					UUID.randomUUID().toString().substring(0,8).toUpperCase(), BigDecimal.valueOf(12.50), sp1IMAX, posto2));
				bigliettoRepository.save(new Biglietto(null, user3,
					UUID.randomUUID().toString().substring(0,8).toUpperCase(), BigDecimal.valueOf(10.00), sp23D, posto3));

				// — biglietti aggiuntivi —
				if (spettacoli.size() > 4 && user4 != null && user5 != null
						&& user7 != null && user8 != null
						&& user9 != null && user10 != null
						&& user11 != null && user12 != null && user13 != null) {

					Spettacolo sp3 = spettacoli.get(2);
					Spettacolo sp4 = spettacoli.get(3);
					Spettacolo sp5 = spettacoli.get(4);
					Spettacolo sp6 = spettacoli.size() > 5 ? spettacoli.get(5) : spettacoli.get(0);
					Spettacolo sp7 = spettacoli.size() > 6 ? spettacoli.get(6) : spettacoli.get(1);
					Spettacolo sp8 = spettacoli.size() > 7 ? spettacoli.get(7) : spettacoli.get(2);

					// user4 – Inception
					salvaBiglietto(user4,  sp3, 1, 4, tuttiIPosti, BigDecimal.valueOf(7.00));
					salvaBiglietto(user4,  sp3, 1, 5, tuttiIPosti, BigDecimal.valueOf(7.00));
					// user5 – Oppenheimer IMAX
					salvaBiglietto(user5,  sp4, 2, 1, tuttiIPosti, BigDecimal.valueOf(10.00));
					// user7 – Spider-Man 3D
					salvaBiglietto(user7,  sp5, 3, 2, tuttiIPosti, BigDecimal.valueOf(15.00));
					salvaBiglietto(user7,  sp5, 3, 3, tuttiIPosti, BigDecimal.valueOf(15.00));
					// user8 – Dune NORMALE
					salvaBiglietto(user8,  sp6, 2, 6, tuttiIPosti, BigDecimal.valueOf(7.00));
					// user9 – Joker NORMALE
					salvaBiglietto(user9,  sp7, 1, 3, tuttiIPosti, BigDecimal.valueOf(7.00));
					// user10 – Top Gun IMAX (sera)
					Spettacolo spSera = spettacoli.get(3); // 20:30 IMAX
					salvaBiglietto(user10, spSera, 4, 1, tuttiIPosti, BigDecimal.valueOf(10.00));
					salvaBiglietto(user10, spSera, 4, 2, tuttiIPosti, BigDecimal.valueOf(10.00));
					// user11 – Avengers IMAX
					salvaBiglietto(user11, sp8, 2, 4, tuttiIPosti, BigDecimal.valueOf(10.00));
					// user12 – Interstellar 3D
					salvaBiglietto(user12, sp1IMAX, 2, 1, tuttiIPosti, BigDecimal.valueOf(12.50));
					// user13 – Inception NORMALE
					salvaBiglietto(user13, sp3, 3, 1, tuttiIPosti, BigDecimal.valueOf(7.00));
				}
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante l'emissione dei biglietti: " + e.getMessage());
		}

		// ==========================================
		// 7. CHAT E MESSAGGI
		// ==========================================
		try {
			if (chatRepository.count() == 0 && user1 != null && staff1 != null) {

				// — chat originale del collega —
				Chat chat1 = chatRepository.save(new Chat(null,
					"Problema visualizzazione biglietto", StatoChat.IN_ATTESA,
					null, true, false, null, user1));
				messaggioRepository.save(new Messaggio(null,
					"Salve, ho pagato ma non vedo il biglietto.", null, chat1, user1));

				// — chat aggiuntive —
				if (user2 != null && user3 != null && user5 != null
						&& user7 != null && user9 != null
						&& staff2 != null && staff3 != null) {

					// Chat 2 – rimborso
					Chat chat2 = chatRepository.save(new Chat(null,
						"Richiesta rimborso biglietto Top Gun",
						StatoChat.APERTO,
						LocalDateTime.now().minusDays(1).minusHours(3),
						false, false, new ArrayList<>(), user2));
					messaggioRepository.save(new Messaggio(null,
						"Buongiorno, ho acquistato due biglietti per Top Gun IMAX ma non riesco a partecipare. È possibile ottenere un rimborso?",
						LocalDateTime.now().minusDays(1).minusHours(3), chat2, user2));
					messaggioRepository.save(new Messaggio(null,
						"Buongiorno! I rimborsi sono disponibili fino a 2 ore prima dello spettacolo. Può fornirmi il codice del biglietto?",
						LocalDateTime.now().minusDays(1).minusHours(2), chat2, staff1));
					messaggioRepository.save(new Messaggio(null,
						"Grazie mille! Il codice è quello che mi è arrivato via email, giusto?",
						LocalDateTime.now().minusDays(1).minusHours(1), chat2, user2));
					messaggioRepository.save(new Messaggio(null,
						"Esatto, il codice alfanumerico di 8 caratteri indicato nell'email di conferma. Lo aspettiamo.",
						LocalDateTime.now().minusDays(1).minusMinutes(30), chat2, staff2));

					// Chat 3 – posto non disponibile
					Chat chat3 = chatRepository.save(new Chat(null,
						"Impossibile selezionare il posto",
						StatoChat.APERTO,
						LocalDateTime.now().minusHours(5),
						false, false, new ArrayList<>(), user3));
					messaggioRepository.save(new Messaggio(null,
						"Salve, sto cercando di prenotare un posto per Interstellar ma il sistema mi dice che è occupato anche se la programmazione è appena uscita.",
						LocalDateTime.now().minusHours(5), chat3, user3));
					messaggioRepository.save(new Messaggio(null,
						"Gentile cliente, controlliamo subito. Potrebbe essere un problema temporaneo di cache. Riprovi tra qualche minuto.",
						LocalDateTime.now().minusHours(4), chat3, staff2));
					messaggioRepository.save(new Messaggio(null,
						"Ho riprovato e ora funziona perfettamente. Grazie mille!",
						LocalDateTime.now().minusHours(3), chat3, user3));

					// Chat 4 – accessibilità (chiusa)
					Chat chat4 = chatRepository.save(new Chat(null,
						"Posti per persone con disabilità",
						StatoChat.CHIUSO,
						LocalDateTime.now().minusDays(2),
						false, false, new ArrayList<>(), user5));
					messaggioRepository.save(new Messaggio(null,
						"Buonasera. Vengo in cinema con mia madre in sedia a rotelle. Ci sono posti adeguati nella sala IMAX?",
						LocalDateTime.now().minusDays(2), chat4, user5));
					messaggioRepository.save(new Messaggio(null,
						"Buonasera! Ogni sala dispone di spazi dedicati alle sedie a rotelle nelle prime file. Basta contattarci telefonicamente per riservarlo.",
						LocalDateTime.now().minusDays(2).plusHours(1), chat4, staff1));
					messaggioRepository.save(new Messaggio(null,
						"Ottimo, grazie per la risposta rapida. Ci vediamo venerdì!",
						LocalDateTime.now().minusDays(2).plusHours(2), chat4, user5));
					messaggioRepository.save(new Messaggio(null,
						"Prego! Buona visione!",
						LocalDateTime.now().minusDays(2).plusHours(3), chat4, staff3));

					// Chat 5 – pagamento fallito (in attesa)
					Chat chat5 = chatRepository.save(new Chat(null,
						"Pagamento non andato a buon fine",
						StatoChat.IN_ATTESA,
						LocalDateTime.now().minusMinutes(40),
						true, false, new ArrayList<>(), user7));
					messaggioRepository.save(new Messaggio(null,
						"Ho provato a pagare con carta di credito ma continua a darmi errore. Il denaro però risulta prelevato dal conto. Cosa devo fare?",
						LocalDateTime.now().minusMinutes(40), chat5, user7));

					// Chat 6 – orari e info
					Chat chat6 = chatRepository.save(new Chat(null,
						"Orari sala IMAX nel weekend",
						StatoChat.APERTO,
						LocalDateTime.now().minusHours(1),
						false, false, new ArrayList<>(), user9));
					messaggioRepository.save(new Messaggio(null,
						"Ciao! Vorrei sapere se ci sono spettacoli IMAX anche la domenica sera o solo il sabato.",
						LocalDateTime.now().minusHours(1), chat6, user9));
					messaggioRepository.save(new Messaggio(null,
						"Ciao! La domenica abbiamo IMAX alle 17:00 e alle 20:30. Trovi tutti gli orari aggiornati nella sezione programmazione del sito.",
						LocalDateTime.now().minusMinutes(20), chat6, staff3));
					messaggioRepository.save(new Messaggio(null,
						"Perfetto, grazie! Ho già acquistato i biglietti per domenica sera.",
						LocalDateTime.now().minusMinutes(5), chat6, user9));
				}
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante la creazione delle chat: " + e.getMessage());
		}

		// ==========================================
		// 8. TOKENS
		// ==========================================
		try {
			if (tokenRepository.count() == 0 && user2 != null) {
				// — token originale del collega —
				tokenRepository.save(new Token("verification-token-sample-xyz", user2));

				// — token aggiuntivi —
				if (user7 != null)  tokenRepository.save(new Token("RESET-" + UUID.randomUUID().toString().substring(0,16).toUpperCase(), user7));
				if (user11 != null) tokenRepository.save(new Token("VERIF-" + UUID.randomUUID().toString().substring(0,16).toUpperCase(), user11));
				if (user13 != null) tokenRepository.save(new Token("RESET-" + UUID.randomUUID().toString().substring(0,16).toUpperCase(), user13));
			}
		} catch (Exception e) {
			System.err.println("❌ Errore durante l'inserimento dei Token: " + e.getMessage());
		}

		System.out.println("===============================================================");
		System.out.println("🚀 DATABASE POPOLATO IN COMPLETA SICUREZZA E SENZA ERRORI!    🚀");
		System.out.println("===============================================================");
	}

	// ── helpers ──────────────────────────────────────────────────────────────

	/** Costruisce un LocalDateTime da una LocalDate con ora e minuto indicati */
	private LocalDateTime ldt(LocalDate giorno, int ora, int minuto) {
		return giorno.atTime(ora, minuto, 0);
	}

	/** Cerca un Genere per nome; se non trovato restituisce il primo della lista */
	private Genere g(List<Genere> generi, String nome) {
		return generi.stream()
				.filter(ge -> ge.getNome().equals(nome))
				.findFirst()
				.orElse(generi.get(0));
	}

	/** Conta i posti di una sala specifica */
	private int postiSala(List<Posto> tutti, Sala sala) {
		return (int) tutti.stream()
				.filter(p -> p.getSala().getId().equals(sala.getId()))
				.count();
	}

	/** Filtra il posto per sala, fila e colonna */
	private Posto filtraPosto(List<Posto> posti, Long salaId, int fila, int colonna) {
		return posti.stream()
				.filter(p -> p.getSala().getId().equals(salaId)
						  && p.getFila()    == fila
						  && p.getColonna() == colonna)
				.findFirst()
				.orElseThrow(() -> new RuntimeException(
					"Posto non trovato per Sala ID: " + salaId + " fila=" + fila + " col=" + colonna));
	}

	/** Crea e salva un biglietto cercando automaticamente il posto nella sala dello spettacolo */
	private void salvaBiglietto(Utente utente, Spettacolo spettacolo,
			int fila, int colonna, List<Posto> tutti, BigDecimal prezzo) {
		Posto posto = filtraPosto(tutti, spettacolo.getSala().getId(), fila, colonna);
		bigliettoRepository.save(new Biglietto(null, utente,
			UUID.randomUUID().toString().substring(0,8).toUpperCase(), prezzo, spettacolo, posto));
	}
}
