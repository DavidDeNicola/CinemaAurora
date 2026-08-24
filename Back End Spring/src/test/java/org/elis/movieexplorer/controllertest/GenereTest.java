package org.elis.movieexplorer.controllertest;

import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.genere.request.EditGenereDTO;
import org.elis.movieexplorer.dto.genere.request.InsertGenereDTO;
import org.elis.movieexplorer.dto.genere.response.ResponseGenereDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenereTest extends GenericTest {
	private final MockMvc mock; 
	private final ObjectMapper mapper = new ObjectMapper();

	
	@Test
	@Order(1)
	@WithMockUser(authorities = "ROLE_Staff")
	public void insertOk() throws Exception {
		InsertGenereDTO dto = new InsertGenereDTO();
		dto.setNome("Cazzeggio");

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.post("/staff/genere")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk(); 
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	@Order(2)
	public void insertBadRequest() throws Exception {
		InsertGenereDTO dto = new InsertGenereDTO();
		dto.setNome("");

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.post("/staff/genere")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError(); 
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	@Order(3)
	public void findAllOk() throws Exception {
		ResponseGenereDTO dto = new ResponseGenereDTO();

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/genere")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful(); 
		ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();
		
		mock.perform(request).andExpectAll(status, isArray);	
	}
	
	@Test
	@Order(4)
	public void findByIdOk() throws Exception {
		ResponseGenereDTO dto = new ResponseGenereDTO();

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/genere/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(5)
	public void findByIdBadRequest() throws Exception {
		ResponseGenereDTO dto = new ResponseGenereDTO();

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/genere/1000")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(6)
	public void findByIdFilmOk() throws Exception {
		ResponseGenereDTO dto = new ResponseGenereDTO();

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/genere/film/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful(); 
		ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();
		
		mock.perform(request).andExpectAll(status, isArray);	
	}

	@Test
	@Order(7)
	public void findByIdFilmBadRequest() throws Exception {
		ResponseGenereDTO dto = new ResponseGenereDTO();

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/genere/1000")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(8)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editByIdOk() throws Exception {
		EditGenereDTO dto = new EditGenereDTO();
		dto.setNome("Anime");

		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/staff/genere/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(9)
	@WithMockUser(authorities = "ROLE_Staff")
	public void editByIdBadRequest() throws Exception {
		EditGenereDTO dto = new EditGenereDTO();
		dto.setNome("");
		
		String json = mapper.writeValueAsString(dto); // Stringa l'oggetto sotto forma di JSON
		
		RequestBuilder request = MockMvcRequestBuilders.get("/staff/genere/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(10)
	@WithMockUser(authorities = "ROLE_Staff")
	public void removeByIdOk() throws Exception {
		
		RequestBuilder request = MockMvcRequestBuilders.delete("/staff/genere/16")
				.contentType(MediaType.APPLICATION_JSON);
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk(); 
		
		mock.perform(request).andExpect(status);	
	}
	
	@Test
	@Order(11)
	@WithMockUser(authorities = "ROLE_Staff")
	public void removeByIdBadRequest() throws Exception {
		
		RequestBuilder request = MockMvcRequestBuilders.delete("/staff/genere/16")
				.contentType(MediaType.APPLICATION_JSON);
		
		ResultMatcher status = MockMvcResultMatchers.status().is4xxClientError(); 
		
		mock.perform(request).andExpect(status);	
	}
	
}
