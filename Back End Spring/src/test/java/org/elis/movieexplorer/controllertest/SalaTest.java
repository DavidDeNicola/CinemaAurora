package org.elis.movieexplorer.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.sala.response.ResponseSalaDTO;
import org.elis.movieexplorer.model.enums.Tipo;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
	public void findAll200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala");
		
		ResultMatcher status = MockMvcResultMatchers.status().isOk();
		ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();
		
		var response = mock.perform(request).andExpectAll(status, isArray).andReturn().getResponse();
		@SuppressWarnings("unchecked")
		List<ResponseSalaDTO> array = mapper.readValue(response.getContentAsString(), List.class);
		assertEquals(8, array.size());
	}


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
		
		
	}

	
	@Test
	public void findByNome200() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/nome/Sala Rossa");
		ResultMatcher status = MockMvcResultMatchers.status().is(200);
		
		mock.perform(request).andExpect(status);
	}
	
	@Test
	public void findByNome404() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/sala/nome/Sala Psichedelica");
		ResultMatcher status = MockMvcResultMatchers.status().is(404);
		
		mock.perform(request).andExpect(status);
	}
	

}
