package org.elis.movieexplorer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.elis.movieexplorer.model.enums.Tipo;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(nullable = false)
	private Tipo tipo;
	
	@OneToMany(mappedBy = "sala")
	private List<Spettacolo> spettacoli;

	@OneToMany(mappedBy = "sala")
	private List<Posto> posti;

}
