package org.elis.movieexplorer.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
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
@Table(
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_spettacolo_posto",
	            columnNames = {"spettacolo_id", "posto_id"}
	        )
	    }
	)
public class Biglietto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Utente utente;
	
	@Column(nullable = false, unique = true)
    private String codiceBiglietto;
	
	@Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Spettacolo spettacolo;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Posto posto;

	@PostRemove
	private void liberaPosto(){
		spettacolo.setPostiRimanenti(spettacolo.getPostiRimanenti()+1);
	}

	@PostPersist
	private void occupaPosti(){
		spettacolo.setPostiRimanenti(spettacolo.getPostiRimanenti()-1);
	}

}
