package org.elis.movieexplorer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Spettacolo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate data;
	
	@Column(nullable = false)
	private LocalDateTime oraInizio;
	
	@Column(nullable = false)
	private LocalDateTime oraFine;
	
	@Column(nullable = false)
	@Range(min = 0)
	private Integer postiRimanenti;
	
	@OneToMany(mappedBy = "spettacolo")
	private List<Biglietto> biglietti;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Sala sala;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Film film;
}
