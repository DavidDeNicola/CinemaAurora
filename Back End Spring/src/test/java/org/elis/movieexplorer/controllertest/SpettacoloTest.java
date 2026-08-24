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
        dto.setData(LocalDate.parse("2026-05-26"));
        dto.setOraInizio(LocalDateTime.parse("2026-05-26T19:30:00"));
        dto.setIdSala(7L);
        dto.setIdFilm(14L);

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
        dto.setIdSala(2L);

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
