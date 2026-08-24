package org.elis.movieexplorer.controllertest;

import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.biglietto.request.InsertBigliettoDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BigliettoTest extends GenericTest{
	private final MockMvc mock;
	private final ObjectMapper mapper = new ObjectMapper();
	
//POST "/cliente/biglietto"
	
	// insertOk
	@Test
	@Order(1)
	//@WithMockUser(authorities = "ROLE_CLIENTE")
	@WithUserDetails("mailprva@gmail.com")
	public void insertOk() throws Exception{
		InsertBigliettoDTO dto = new InsertBigliettoDTO();
		dto.setIdSpettacolo(1L);
		dto.setNumeroBiglietti(1);
		
		String json = mapper.writeValueAsString(dto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/cliente/biglietto")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
			
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		
		mock.perform(request).andExpect(status);
	}
	
	// insertBigliettiInsufficenti
	@Test
	@Order(2)
	@WithUserDetails("mailprva@gmail.com")
	public void insertBigliettiInsufficenti() throws Exception{
		InsertBigliettoDTO dto = new InsertBigliettoDTO();
		dto.setIdSpettacolo(1L);
		dto.setNumeroBiglietti(999);
		
		String json = mapper.writeValueAsString(dto);
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/cliente/biglietto")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
				
		ResultMatcher status = MockMvcResultMatchers.status().isUnprocessableEntity();
		mock.perform(request).andExpect(status);
	}
// ==============================================================================================	
	
// GET "/staff/biglietto/utente/{id}"
	
	// findByIdUtenteOk	
	
	@Test
	@Order(3)
	@WithUserDetails("luca.bianchi@movieexplorer.it")
	public void findByUtenteOk() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.get("/staff/biglietto/utente/4");
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		ResultMatcher content = MockMvcResultMatchers.jsonPath("$").isArray();
		
		mock.perform(request).andExpectAll(status, content);
	}
	
	// findByIdUtenteNoContent
	@Test
	@Order(4)
	@WithUserDetails("luca.bianchi@movieexplorer.it")	
	public void findByUtenteNoContent() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.get("/staff/biglietto/utente/999");
		ResultMatcher status = MockMvcResultMatchers.status().isNoContent();
		mock.perform(request).andExpect(status);
	}
	
// ==============================================================================================	
// GET "staff/biglietto/spettacolo/{id}"
	
	//findBySpettacoloOk
	@Test
	@Order(5)
	@WithUserDetails("luca.bianchi@movieexplorer.it")
	public void findByIdSpettacoloOk() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.get("/staff/biglietto/spettacolo/1");
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		ResultMatcher content = MockMvcResultMatchers.jsonPath("$").isArray();
		
		mock.perform(request).andExpectAll(status, content);
	}
	
	//findByIdSpettacoloNoContent
	@Test
	@Order(6)
	@WithUserDetails("luca.bianchi@movieexplorer.it")
	public void findByIdSpettacoloNoContent() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.get("/staff/biglietto/spettacolo/99999");
		
		ResultMatcher status = MockMvcResultMatchers.status().isNotFound();
		
		mock.perform(request).andExpect(status);
	}
	
// ==============================================================================================	

// DELETE "/cliente/biglietto/{id}"
	
	@Test
	@Order(7)
	@WithUserDetails("sara.colombo@email.it")
	public void deleteOk() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.delete("/cliente/biglietto/11")
				.content("sara.colombo@email.it")
				.contentType(MediaType.APPLICATION_JSON);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		
		mock.perform(request).andExpect(status);
	}
	
	//deleteBigliettoNotFound
	@Test
	@Order(8)
	@WithUserDetails("mailprva@gmail.com")
	public void deleteBigliettoNotFound() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.delete("/cliente/biglietto/999")
				.content("mailprva@gmail.com")
				.contentType(MediaType.APPLICATION_JSON);
		
		ResultMatcher status = MockMvcResultMatchers.status().isNotFound();
		
		mock.perform(request).andExpect(status);
	}
	
	
	// deleteSpettacoloPassato
	@Test
	@Order(9)
	@WithUserDetails("mailprva@gmail.com")
	public void deleteSpettacoloPassato() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.delete("/cliente/biglietto/1")
				.content("mailprva@gmail.com")
				.contentType(MediaType.APPLICATION_JSON);
		
		ResultMatcher status = MockMvcResultMatchers.status().isUnprocessableEntity();
		
		mock.perform(request).andExpect(status);
	}	
}
