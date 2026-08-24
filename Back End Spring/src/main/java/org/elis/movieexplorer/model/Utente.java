package org.elis.movieexplorer.model;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.elis.movieexplorer.model.enums.Ruolo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode

public class Utente implements UserDetails {
	@Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@EqualsAndHashCode.Exclude
	private Ruolo ruolo;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String cognome;
	
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	
	@OneToMany(mappedBy = "utente")
	private List<Biglietto> biglietti;

	@OneToMany( mappedBy = "mittente" )
	private List<Messaggio> messaggi;
	
	@OneToMany( mappedBy = "utente" )
	private List<Chat> chat;
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+ruolo.getNome());
		return List.of(authority);
	}

	@Override
	public String getUsername() {
		return email;
	}
}