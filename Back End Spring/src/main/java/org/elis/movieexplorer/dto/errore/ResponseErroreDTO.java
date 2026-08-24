package org.elis.movieexplorer.dto.errore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResponseErroreDTO {
    private String path;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
}
