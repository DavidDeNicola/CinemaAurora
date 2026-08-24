package org.elis.movieexplorer.dto.errore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResponseErroreValidationDTO {
    private String path;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, String> errori;
}
