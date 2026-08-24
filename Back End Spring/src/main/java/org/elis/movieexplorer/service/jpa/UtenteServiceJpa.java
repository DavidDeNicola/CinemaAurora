package org.elis.movieexplorer.service.jpa;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.dto.resetPassword.request.EditPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.request.ResetPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.response.ResetPasswordResponse;
import org.elis.movieexplorer.dto.utente.request.EditUtenteDTO;
import org.elis.movieexplorer.dto.utente.request.InsertUtenteDTO;
import org.elis.movieexplorer.dto.utente.request.LoginRequestDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDataDTO;
import org.elis.movieexplorer.exception.definition.MEBadRequestException;
import org.elis.movieexplorer.exception.definition.MENotFoundException;
import org.elis.movieexplorer.exception.definition.MERegistrationErrorException;
import org.elis.movieexplorer.mapper.UtenteMapper;
import org.elis.movieexplorer.model.Token;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.model.enums.Ruolo;
import org.elis.movieexplorer.repository.TokenRepository;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.elis.movieexplorer.security.JwtUtils;
import org.elis.movieexplorer.service.definition.BigliettoService;
import org.elis.movieexplorer.service.definition.UtenteService;
import org.elis.movieexplorer.service.email.EmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "service.impl", havingValue = "JPA")
@RequiredArgsConstructor
public class UtenteServiceJpa implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final TokenRepository tokenRepository;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final BigliettoService bigliettoService;
    private final EmailService emailService;
    
    @Transactional
    @Override
    public ResponseUtenteDTO insertCliente(InsertUtenteDTO dto) {
        return registraUtente(dto, Ruolo.CLIENTE);
    }

    @Transactional
    @Override
    public ResponseUtenteDTO insertStaff(InsertUtenteDTO dto) {
        return registraUtente(dto, Ruolo.STAFF);
    }

    @Override
    public Boolean checkEmailAvailability(String email) {
        Optional<Utente> optional = utenteRepository.findUtenteByEmail(email);
        return optional.isEmpty();
    }

    @Override
    public List<ResponseUtenteDataDTO> findAllCliente() {
        List<Utente> utenti = utenteRepository.findAll();
        return utenteMapper.toResponseList(utenti);
    }

    
    
    
    @Override
    public List<ResponseUtenteDataDTO> findAllStaff() {
    	List<Utente> staff = utenteRepository.findAllByRuolo(Ruolo.STAFF);
    	return utenteMapper.toResponseList(staff);
    }
    
    
    
    
    
    
    
    @Override
    public ResponseUtenteDataDTO findById(Long id) {
        Optional<Utente> optional = utenteRepository.findById(id);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));
        return utenteMapper.toResponseUtente(u);
    }

    @Override
    public ResponseUtenteDataDTO findByEmail(String email) {
        Optional<Utente> optional = utenteRepository.findUtenteByEmail(email);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));
        return utenteMapper.toResponseUtente(u);
    }

    @Override
    @Transactional
    public ResponseUtenteDTO editById(Long id, EditUtenteDTO uMod) {
        Optional<Utente> optional = utenteRepository.findById(id);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));
        u.setEmail(uMod.getEmail());
        u.setNome(uMod.getNome());
        u.setCognome(uMod.getCognome());
        utenteRepository.save(u);
        return new ResponseUtenteDTO("Dati modificati con successo.");
    }

    @Override
    @Transactional
    public ResponseUtenteDTO removeById(Long id) {
        Optional<Utente> optional = utenteRepository.findById(id);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));
        u.getBiglietti().forEach(b -> bigliettoService.removeByIdEmail(b.getId(), u.getEmail()));
        utenteRepository.delete(u);
        return new ResponseUtenteDTO("Utente eliminato con successo.");
    }

    @Override
    @Transactional
    public ResponseUtenteDTO removeStaffById(Long id) {
        Optional<Utente> optional = utenteRepository.findById(id);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));

        if (u.getRuolo() != Ruolo.STAFF) {
            throw new MEBadRequestException("Puoi eliminare solo utenti staff da questa sezione.");
        }

        utenteRepository.delete(u);
        return new ResponseUtenteDTO("Staff eliminato con successo.");
    }

    @Override
    @Transactional
    public ResponseUtenteDTO removeStaffByEmail(String email) {
        Optional<Utente> optional = utenteRepository.findUtenteByEmail(email);
        Utente u = optional.orElseThrow(() -> new MENotFoundException("Utente non trovato."));

        if (u.getRuolo() != Ruolo.STAFF) {
            throw new MEBadRequestException("Puoi eliminare solo utenti staff da questa sezione.");
        }

        utenteRepository.delete(u);
        return new ResponseUtenteDTO("Staff eliminato con successo.");
    }

    @Override
    public String login(LoginRequestDTO request) {
        Optional<Utente> optional = utenteRepository.findUtenteByEmail(request.getEmail());
        Utente utente = optional.orElseThrow(() -> new MEBadRequestException("Password o email errate"));

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword()))
            throw new MEBadRequestException("Password o email errate");

        return jwtUtils.createToken(utente);
    }

    @Override
    public ResetPasswordResponse editPassword(EditPasswordRequest request) {
        Utente utente = utenteRepository.findUtenteByEmail(request.getEmail())
        		.orElseThrow(() -> new MEBadRequestException("Email non trovata"));

        if (!passwordEncoder.matches(request.getPasswordVecchia(), utente.getPassword()))
            throw new MEBadRequestException("Password errata");

        utente.setPassword(passwordEncoder.encode(request.getPasswordNuova()));


        utenteRepository.save(utente);

        return new ResetPasswordResponse("Password cambiata con successo.");
    }

    private ResponseUtenteDTO registraUtente(InsertUtenteDTO dto, Ruolo ruolo) {
        String password = dto.getPassword();
        String confPassword = dto.getConfermaPassword();
        String email = dto.getEmail();

        if (!checkEmailAvailability(email))
            throw new MERegistrationErrorException("Email già presente nel sistema.");

        if (!password.equals(confPassword))
            throw new MERegistrationErrorException("Le password non coincidono");

        dto.setPassword(passwordEncoder.encode(password));
        Utente user = utenteMapper.fromInsertUtenteDTO(dto);
        user.setRuolo(ruolo);
        utenteRepository.save(user);

        return new ResponseUtenteDTO("Utente registrato con successo.");
    }

	@Override
	@Transactional
	public String invioResetPassword(String email) {
		Optional<Utente> utenteOpt = utenteRepository.findUtenteByEmail(email);
        if(utenteOpt.isPresent()){
            Utente utente = utenteOpt.get();
            String token = UUID.randomUUID().toString();

            Token tk = new Token(token, utente);
            tokenRepository.save(tk);

            emailService.simpleMail(System.getenv("SMTP_EMAIL"), email, token);
        }
		String ret = "Email inviata con successo";
		
		return ret;
	}

	@Override
	@Transactional
	public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        Optional<Utente> utenteOpt = tokenRepository.getUtenteByToken(request.getToken());
        Utente utente = utenteOpt.orElseThrow(() -> new MENotFoundException("Token non valido"));

		Optional<Token> tokenOptional = tokenRepository.getTokenByUtente_Id(utente.getId());
		
		if(tokenOptional.isEmpty()) {
			throw new MEBadRequestException("Token non valido");
		}

        Token token = tokenOptional.get();

		if(!token.getToken().equals(request.getToken()) || token.getScadenza().isBefore(LocalDateTime.now())) {
			throw new MEBadRequestException("Token non valido o scaduto");
		}
		
		if(!request.getPassword().equals(request.getConfermaPassword())) {
			throw new MEBadRequestException("Le password non corrispondono");
		}
				
		utente.setPassword(passwordEncoder.encode(request.getPassword()));
		utenteRepository.save(utente);

        tokenRepository.delete(token);
		
		return new ResetPasswordResponse("Password resettata con successo.");
	}
}
