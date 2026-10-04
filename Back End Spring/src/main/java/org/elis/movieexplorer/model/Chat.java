package org.elis.movieexplorer.model;

import java.time.LocalDateTime;
import java.util.List;

import org.elis.movieexplorer.model.enums.StatoChat;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@Column(nullable = false)
	private String oggetto;

	@Column(nullable = false)
	private StatoChat stato = StatoChat.IN_ATTESA;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private boolean messaggiSospesoPerStaff = true;

	@Column(nullable = false)
	private boolean messaggiSospesoPerCliente = false;
	
	@OneToMany( mappedBy = "chat" )
	private List<Messaggio> messaggi;
	
	@ManyToOne
	private Utente utente;
	
}
