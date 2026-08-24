package org.elis.movieexplorer.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.dto.film.request.EditFilmDTO;
import org.elis.movieexplorer.dto.film.request.InsertFilmDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmTest extends GenericTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private static Long idGladiatore;

    @Test
    @Order(1)
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertOk() throws Exception {
        InsertFilmDTO dto = new InsertFilmDTO();
        dto.setTitolo("Il Gladiatore II");
        dto.setDescrizione("Anni dopo aver assistito alla morte dell'eroe Massimo, Lucio è costretto a entrare nel Colosseo.");
        dto.setDurata(150);
        dto.setAttori("Paul Mescal, Denzel Washington");
        dto.setUrlLocandina("https://image.it/gladiatore.jpg");
        dto.setIdGeneri(List.of(1L));

        String json = mapper.writeValueAsString(dto);
        RequestBuilder request = MockMvcRequestBuilders.post("/staff/film")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        ResultMatcher checkTitle = MockMvcResultMatchers.jsonPath("$.titolo").value("Il Gladiatore II");

        MvcResult result = mockMvc.perform(request)
                .andExpectAll(status, checkTitle)
                .andReturn();

        String response = result.getResponse().getContentAsString();
        idGladiatore = ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertConflict() throws Exception {
        InsertFilmDTO dto = new InsertFilmDTO();
        dto.setTitolo("Il Cavaliere Oscuro");
        dto.setDescrizione("Batman si trova ad affrontare il suo nemico più temibile: il Joker, un criminale psicopatico che vuole gettare Gotham City nel caos totale.");
        dto.setDurata(152);
        dto.setAttori("Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine, Maggie Gyllenhaal");
        dto.setUrlLocandina("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg");
        dto.setIdGeneri(List.of(1L));

        String json = mapper.writeValueAsString(dto);
        RequestBuilder request = MockMvcRequestBuilders.post("/staff/film")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isConflict();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertBadRequest() throws Exception {
        InsertFilmDTO dto = new InsertFilmDTO();
        dto.setTitolo("");

        String json = mapper.writeValueAsString(dto);
        RequestBuilder request = MockMvcRequestBuilders.post("/staff/film")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isBadRequest();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Order(4)
    @WithMockUser(authorities = "ROLE_Staff")
    public void findAll2xx() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/staff/film");
        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();

        mockMvc.perform(request).andExpectAll(status, isArray);
    }

    @Test
    @Order(5)
    @WithMockUser(authorities = "ROLE_Staff")
    public void findByIdOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/staff/film/" + idGladiatore);
        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        ResultMatcher checkTitle = MockMvcResultMatchers.jsonPath("$.titolo").value("Il Gladiatore II");

        mockMvc.perform(request).andExpectAll(status, checkTitle);
    }

    @Test
    @Order(6)
    @WithMockUser(authorities = "ROLE_Staff")
    public void editByIdOk() throws Exception {
        EditFilmDTO dto = new EditFilmDTO();
        dto.setTitolo("Il Gladiatore II Modificato");

        String json = mapper.writeValueAsString(dto);
        RequestBuilder request = MockMvcRequestBuilders.patch("/staff/film/" + idGladiatore)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Order(7)
    @WithMockUser(authorities = "ROLE_Staff")
    public void removeByIdOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/staff/film/" + idGladiatore)
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Order(8)
    @WithMockUser(authorities = "ROLE_Staff")
    public void removeByIdNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/staff/film/999999")
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().isNotFound();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @WithMockUser(authorities = "ROLE_Staff")
    public void findByTitolo2xx() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.get("/staff/film/titolo?titolo=Pulp")
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();
        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @WithMockUser(authorities = "ROLE_Staff")
    public void findByTitoloNotAuthorized() throws Exception{
        RestClient restClient = RestClient.create("https://www.omdbapi.com");
        Assertions.assertThatThrownBy(()->{
            restClient.get()
                    .uri(t ->
                        t.queryParam("apikey", "ciao")
                         .queryParam("t", "Ciao").build())
                    .retrieve().toBodilessEntity();
        }).isInstanceOf(HttpClientErrorException.Unauthorized.class);
    }
}