package org.elis.movieexplorer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
