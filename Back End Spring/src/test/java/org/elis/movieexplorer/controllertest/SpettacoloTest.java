package org.elis.movieexplorer.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.GenericTest;
import org.elis.movieexplorer.customOrder.CustomMethodOrder;
import org.elis.movieexplorer.customOrder.Dnd;
import org.elis.movieexplorer.customOrder.Jojo;
import org.elis.movieexplorer.dto.spettacolo.request.EditSpettacoloDTO;
import org.elis.movieexplorer.dto.spettacolo.request.InsertSpettacoloDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@TestMethodOrder(CustomMethodOrder.class)
public class SpettacoloTest extends GenericTest {
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    @Test
    @Jojo(jojoChar = "Johnny")
    @Dnd(classe = "Lottatore")
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertOk() throws Exception {
        InsertSpettacoloDTO dto = new InsertSpettacoloDTO();
        dto.setData(LocalDate.now().plusDays(5));
        dto.setOraInizio(LocalDateTime.now().plusDays(5).withSecond(0).withNano(0));
        dto.setIdSala(1L);
        dto.setIdFilm(1L);

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/staff/spettacolo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();
        ResultMatcher oraFinePresente = MockMvcResultMatchers.jsonPath("$.oraFine").exists();

        mockMvc.perform(request).andExpectAll(status, oraFinePresente);
    }

    @Test
    @Jojo(jojoChar = "Jotaro")
    @Dnd(classe = "Stregone")
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertConflict() throws Exception {
        InsertSpettacoloDTO dto = new InsertSpettacoloDTO();

        // InitRunConfig crea sempre: tra 7 giorni, sala 1, Top Gun dalle 15:00 alle 17:10
        LocalDate tra7 = LocalDate.now().plusDays(7);
        dto.setData(tra7);
        dto.setOraInizio(tra7.atTime(16, 0));
        dto.setIdSala(1L);
        dto.setIdFilm(1L);

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders.post("/staff/spettacolo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isConflict();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Joseph")
    @Dnd(classe = "Bardo")
    @WithMockUser(authorities = "ROLE_Staff")
    public void insertBadRequest() throws Exception {
        InsertSpettacoloDTO dto = new InsertSpettacoloDTO();
        dto.setData(LocalDate.now().plusDays(5));
        dto.setOraInizio(LocalDateTime.now().plusDays(4));
        dto.setIdSala(1L);
        dto.setIdFilm(1L);

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders.post("/staff/spettacolo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isBadRequest();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Jolyne")
    @Dnd(classe = "Monaco")
    public void findAll2xx() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/spettacolo");

        ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();
        ResultMatcher isArray = MockMvcResultMatchers.jsonPath("$").isArray();

        mockMvc.perform(request).andExpectAll(status, isArray);
    }

    @Test
    @Jojo(jojoChar = "Giorno")
    @Dnd(classe = "Paladino")
    public void findByData2xx() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/spettacolo/2026-04-23");

        ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Josuke")
    @Dnd(classe = "Barbaro")
    public void findByFilmId2xx() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/spettacolo/film/1");

        ResultMatcher status = MockMvcResultMatchers.status().is2xxSuccessful();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Jonathan")
    @Dnd(classe = "Stregone")
    @WithMockUser(authorities = "ROLE_Staff")
    public void editByIdOk() throws Exception {
        EditSpettacoloDTO dto = new EditSpettacoloDTO();
        dto.setData(LocalDate.now().plusMonths(3));
        dto.setOraInizio(LocalDateTime.now().plusMonths(3));
        dto.setOraFine(dto.getOraInizio().plusHours(2));

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders.patch("/staff/spettacolo/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Jotaro")
    @Dnd(classe = "Bardo")
    @WithMockUser(authorities = "ROLE_Staff")
    public void editByIdConflict() throws Exception {
        EditSpettacoloDTO dto = new EditSpettacoloDTO();
        
        // InitRunConfig crea sempre in sala 1, oggi: spettacolo 1 (10:00) e spettacolo 4 (20:30 → 22:40)
        // sposto lo spettacolo 1 alle 20:00 → 22:00: si sovrappone al 4
        dto.setOraInizio(LocalDate.now().atTime(20, 0));
        dto.setOraFine(LocalDate.now().atTime(22, 0));

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders.patch("/staff/spettacolo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isConflict();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Johnny")
    @Dnd(classe = "Lottatore")
    @WithMockUser(authorities = "ROLE_Staff")
    public void editByIdNotFound() throws Exception {
        EditSpettacoloDTO dto = new EditSpettacoloDTO();
        dto.setIdSala(2L);

        String json = mapper.writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders.patch("/staff/spettacolo/10000000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ResultMatcher status = MockMvcResultMatchers.status().isNotFound();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Josuke")
    @Dnd(classe = "Barbaro")
    @WithMockUser(authorities = "ROLE_Staff")
    public void editByBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.patch("/staff/spettacolo/1")
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().isBadRequest();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Giorno")
    @Dnd(classe = "Monaco")
    @WithMockUser(authorities = "ROLE_Staff")
    public void removeByIdOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/staff/spettacolo/1")
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().isOk();

        mockMvc.perform(request).andExpect(status);
    }

    @Test
    @Jojo(jojoChar = "Jolyne")
    @Dnd(classe = "Paladino")
    @WithMockUser(authorities = "ROLE_Staff")
    public void removeById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/staff/spettacolo/200000000000")
                .contentType(MediaType.APPLICATION_JSON);

        ResultMatcher status = MockMvcResultMatchers.status().isNotFound();

        mockMvc.perform(request).andExpect(status);
    }
}
