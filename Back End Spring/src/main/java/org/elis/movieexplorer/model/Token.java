package org.elis.movieexplorer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime scadenza;

    @OneToOne
    private Utente utente;

    public Token(String token, Utente utente){
        this.token = token;
        this.scadenza = LocalDateTime.now().plusMinutes(15);
        this.utente = utente;
    }

}
