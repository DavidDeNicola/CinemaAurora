package org.elis.movieexplorer.service.definition;

import java.util.List;

import org.elis.movieexplorer.dto.utente.request.InsertUtenteDTO;
import org.elis.movieexplorer.dto.utente.request.LoginRequestDTO;
import org.elis.movieexplorer.dto.resetPassword.request.EditPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.request.ResetPasswordRequest;
import org.elis.movieexplorer.dto.resetPassword.response.ResetPasswordResponse;
import org.elis.movieexplorer.dto.utente.request.EditUtenteDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDTO;
import org.elis.movieexplorer.dto.utente.response.ResponseUtenteDataDTO;

public interface UtenteService {
    // CREATE
    ResponseUtenteDTO insertCliente(InsertUtenteDTO dto);
    
    ResponseUtenteDTO insertStaff(InsertUtenteDTO dto);

    // READ
    List<ResponseUtenteDataDTO> findAllCliente();

    ResponseUtenteDataDTO findById(Long id);

    ResponseUtenteDataDTO findByEmail(String email);

    // UPDATE
    ResponseUtenteDTO editById(Long id, EditUtenteDTO uMod);
    String invioResetPassword(String email);

    // DELETE
    ResponseUtenteDTO removeById(Long id);

    ResponseUtenteDTO removeStaffById(Long id);

    ResponseUtenteDTO removeStaffByEmail(String email);

    // UTILITY
    Boolean checkEmailAvailability(String email);
    
    String login(LoginRequestDTO request);

    ResetPasswordResponse editPassword(EditPasswordRequest request);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);

	List<ResponseUtenteDataDTO> findAllStaff();
}
