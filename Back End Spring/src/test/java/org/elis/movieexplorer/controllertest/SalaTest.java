package org.elis.movieexplorer.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.sala.request.EditSalaDTO;
import org.elis.movieexplorer.dto.sala.request.InsertSalaDTO;
import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.model.enums.Tipo;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class SalaTest extends GenericTest {
	private final MockMvc mock; 
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@Order(2)
	@WithMockUser(authorities = "ROLE_Staff")
	public void insert200()  throws Exception {

		InsertSalaDTO dto = new InsertSalaDTO();
		dto.setNome("Andromeda");
		dto.setNumeroPosti((short) 248);
		dto.setTipo(Tipo.NORMALE);
		
		String json = mapper.writeValueAsString(dto);
		RequestBuilder request = MockMvcRequestBuilders.post("/staff/sala")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		mock.perform(request).andExpect(status);
		
	}
	
	@Test
	@Order(3)
	@WithMockUser(authorities = "ROLE_Staff")
	public void insert409()  throws Exception {
		
		InsertSalaDTO dto = new InsertSalaDTO();
		dto.setNome("Andromeda");
		dto.setNumeroPosti((short) 248);
		dto.setTipo(Tipo.NORMALE);
		
		String json = mapper.writeValueAsString(dto);
		RequestBuilder request = MockMvcRequestBuilders.post("/staff/sala")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(409);
		mock.perform(request).andExpect(status);
		
	}
	
	@Test
	@Order(3)
	@WithMockUser(authorities = "ROLE_Staff")
	public void insert400()  throws Exception {

		InsertSalaDTO dto = new InsertSalaDTO();
		dto.setNome("");
		dto.setNumeroPosti((short) 248);
		dto.setTipo(Tipo.NORMALE);
		
		String json = mapper.writeValueAsString(dto);
		RequestBuilder request = MockMvcRequestBuilders.post("/staff/sala")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(400);
		mock.perform(request).andExpect(status);
	
	}
	
	@Test
	@Order(2)
	public void findAll200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala");
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();
		
		var response = mock.perform(request).andExpectAll(status, isArray).andReturn().getResponse();
		@SuppressWarnings("unchecked")
		List<ResponseSalaDTO> array = mapper.readValue(response.getContentAsString(), List.class);
//		Assertions.assertThat(array.size()).isEqualTo(7);
		assertEquals(7, array.size());
	}
	
	// Test provato e funzionante
//	@Test   
//	@Order(1)
//	public void findAll204() throws Exception {
//		RequestBuilder request = MockMvcRequestBuilders.get("/sala");
//		ResultMatcher status = MockMvcResultMatchers.status().isNoContent();
//
//		mock.perform(request).andExpect(status);
//	}
	
	@Test
	public void findById200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/2");
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);

	}
	
	@Test
	public void findById404() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/200");
		ResultMatcher status = MockMvcResultMatchers.status().is(404);
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	public void findByTipo200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/tipo/NORMALE");
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);
		
		//Modo piu specifico
//		ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();
//		var response = mock.perform(request).andExpectAll(status, isArray).andReturn().getResponse();
//		@SuppressWarnings("unchecked")
//		List<ResponseSalaDTO> array = mapper.readValue(response.getContentAsString(), List.class);
//		assertEquals(4, array.size());
		
	}

	// Test provato e funzionante
//	@Test
//	@Order(1)
//	public void findByTipo204() throws Exception {
//		RequestBuilder request = MockMvcRequestBuilders.get("/sala/tipo/NORMALE");
//		ResultMatcher status = MockMvcResultMatchers.status().is(204);
//		
//		mock.perform(request).andExpect(status);
//	}
	
	@Test
	public void findByNome200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/nome/Sala Mercurio");
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	public void findByNome404() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/nome/Sala Psichedelica");
		ResultMatcher status = MockMvcResultMatchers.status().is(404);
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	@Order(3)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editById200() throws Exception {
		
		EditSalaDTO dtoEdit = new EditSalaDTO();
		dtoEdit.setNome("Sala Andromeda");
		dtoEdit.setNumeroPosti(Short.valueOf("100"));
		dtoEdit.setTipo(Tipo.TRED);
		String json = mapper.writeValueAsString(dtoEdit);
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/staff/sala/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);

	}
	
	@Test
	@Order(2)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editById404() throws Exception {
		
		EditSalaDTO dtoEdit = new EditSalaDTO();
		dtoEdit.setNome("Sala Andromeda");
		dtoEdit.setNumeroPosti(Short.valueOf("100"));
		dtoEdit.setTipo(Tipo.TRED);
		String json = mapper.writeValueAsString(dtoEdit);
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/staff/sala/1549")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(404);
		
		mock.perform(request).andExpect(status);
		
	}
	
	@Test
	@Order(2)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editById409() throws Exception {
		
		EditSalaDTO dtoEdit = new EditSalaDTO();
		dtoEdit.setNome("Sala Giove");
		dtoEdit.setNumeroPosti(Short.valueOf("100"));
		dtoEdit.setTipo(Tipo.TRED);
		String json = mapper.writeValueAsString(dtoEdit);
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/staff/sala/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		ResultMatcher status = MockMvcResultMatchers.status().is(409);
		
		mock.perform(request).andExpect(status);
		
	}
	
	@Test
	@Order(2)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editById400() throws Exception {
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/staff/sala/1");
				
		ResultMatcher status = MockMvcResultMatchers.status().isBadRequest();
		
		mock.perform(request).andExpect(status);
		
	}
	
	@Test
	@Order(3)
	@WithMockUser(authorities = "ROLE_Staff")
	public void removeById200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.delete("/staff/sala/1");
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	@Order(3)
	@WithMockUser(authorities = "ROLE_Staff")
	public void removeById404() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.delete("/staff/sala/34591");
		ResultMatcher status = MockMvcResultMatchers.status().is(404);
		
		mock.perform(request).andExpect(status);
	}
}
