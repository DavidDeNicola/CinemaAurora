package org.elis.movieexplorer.controllertest;

import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.resetPassword.request.ResetPasswordRequest;
import org.elis.movieexplorer.dto.utente.request.InsertUtenteDTO;
import org.elis.movieexplorer.dto.utente.request.LoginRequestDTO;
import org.elis.movieexplorer.model.Token;
import org.elis.movieexplorer.model.Utente;
import org.elis.movieexplorer.repository.TokenRepository;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class UtenteTest extends GenericTest {
	private final MockMvc mock;
	private final UtenteRepository utenteRepository;
	private final TokenRepository tokenRepository;
	private final ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	@Order(1)
	public void registerOk() throws Exception {
		InsertUtenteDTO dto = new InsertUtenteDTO();
		dto.setNome("Dario");
		dto.setCognome("Balella");
		dto.setEmail("d.balella@elis.org");
		dto.setPassword("Password1!");
		dto.setConfermaPassword("Password1!");

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.post("/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk(); // oppure is2xxSuccessful()
		
		mock.perform(request).andExpect(status);
	}
	
	
	@Test
	@Order(2)
	public void registerBadRequest() throws Exception {
		InsertUtenteDTO dto = new InsertUtenteDTO();
		dto.setNome("");
		dto.setCognome(" ");
		dto.setEmail("d.balella.org");
		dto.setPassword("Password1");
		dto.setConfermaPassword("Password");

		String json = mapper.writeValueAsString(dto);
		
		RequestBuilder request = MockMvcRequestBuilders.post("/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError();
		
		mock.perform(request).andExpect(status);
	}
	
	
	@Test
	@Order(3)
	public void loginOk() throws Exception {
		LoginRequestDTO dto = new LoginRequestDTO();
		dto.setEmail("d.balella@elis.org");
		dto.setPassword("Password1!");

		String json = mapper.writeValueAsString(dto);
		
		RequestBuilder request = MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();
		ResultMatcher header = MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION);
		
		mock.perform(request).andExpectAll(status, header);
	}
	
	
	@Test
	@Order(4)
//	@WithMockUser(authorities = "ROLE_SuperAdmin")
	@WithUserDetails("admin@gmail.com")
	public void addStaffOk() throws Exception {
		InsertUtenteDTO dto = new InsertUtenteDTO();
		dto.setNome("Admin");
		dto.setCognome("Super");
		dto.setEmail("admin@elis.org");
		dto.setPassword("Password1!");
		dto.setConfermaPassword("Password1!");
		
		String json = mapper.writeValueAsString(dto);
		
		RequestBuilder request = MockMvcRequestBuilders.post("/admin/aggiungi_staff")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();
		
		mock.perform(request).andExpectAll(status);
	}
	
	@Test
    @Order(5)
    @WithUserDetails("d.balella@elis.org") // Un utente cliente normale, non admin
    public void addStaffForbidden() throws Exception {
        InsertUtenteDTO dto = new InsertUtenteDTO();
        dto.setNome("Nuovo");
        dto.setCognome("Staff");
        dto.setEmail("staff.test@elis.org");
        dto.setPassword("Password1!");
        dto.setConfermaPassword("Password1!");
        
        String json = mapper.writeValueAsString(dto);
        
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/aggiungi_staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        
        // errore 403 perché l'utente non ha i permessi
        ResultMatcher status = MockMvcResultMatchers.status().isForbidden();
        
        mock.perform(request).andExpect(status);
    }
	
	@Test
	@Order(6)
	public void resetPasswordOk() throws Exception {
		Optional<Utente> utenteOpt = utenteRepository.findUtenteByEmail("d.balella@elis.org");
		if(utenteOpt.isPresent()) {
			Utente utente = utenteOpt.get();
			String token = UUID.randomUUID().toString();
			Token tk = new Token(token, utente);
			tokenRepository.save(tk);

			ResetPasswordRequest dto = new ResetPasswordRequest();
			dto.setToken(token);
			dto.setPassword("Password1!");
			dto.setConfermaPassword("Password1!");

			String json = mapper.writeValueAsString(dto);

			RequestBuilder request = MockMvcRequestBuilders.patch("/reset_password")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json);

			// 200 OK
			ResultMatcher status = MockMvcResultMatchers.status().isOk();

			mock.perform(request).andExpect(status);
		}
	}
}