package org.elis.movieexplorer.mapper;

import org.elis.movieexplorer.dto.utente.request.InsertUtenteDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDataDTO;
import org.elis.movieexplorer.model.Utente;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UtenteMapper {
    public Utente fromInsertUtenteDTO(InsertUtenteDTO dto) {
        Utente user = new Utente();
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public List<ResponseUtenteDataDTO> toResponseList(List<Utente> utenti) {
        List<ResponseUtenteDataDTO> listResponse = new ArrayList<>();
        for(Utente u:utenti){
            ResponseUtenteDataDTO dto = new ResponseUtenteDataDTO();
            dto.setId(u.getId());
            dto.setNome(u.getNome());
            dto.setCognome(u.getCognome());
            dto.setEmail(u.getEmail());
            listResponse.add(dto);
        }
        return listResponse;
    }

    public ResponseUtenteDataDTO toResponseUtente(Utente u) {
        ResponseUtenteDataDTO dto = new ResponseUtenteDataDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setCognome(u.getCognome());
        dto.setEmail(u.getEmail());
        return dto;
    }
}
