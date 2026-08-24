package org.elis.movieexplorer.model;

import java.time.LocalDateTime;

import org.elis.movieexplorer.model.enums.Ruolo;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Messaggio {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY ) 
	private Long id;

	@Column(nullable = false)
	private String messaggio;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	private Chat chat;
	
	@ManyToOne
	private Utente mittente;
	
	@PostPersist
    public void afterPersist() {
        if( this.mittente.getRuolo() == Ruolo.CLIENTE ){
        	chat.setMessaggiSospesoPerStaff(true);
        }
        else {
        	chat.setMessaggiSospesoPerCliente(true);
        }
    }
	
}
