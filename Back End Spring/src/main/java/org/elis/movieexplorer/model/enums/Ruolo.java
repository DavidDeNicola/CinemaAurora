package org.elis.movieexplorer.model.enums;

import lombok.Getter;

@Getter
public enum Ruolo {
	SUPERADMIN("SuperAdmin"),
	STAFF("Staff"),
	CLIENTE("Cliente");
	
	private String nome;

	Ruolo(String nome) {
		this.nome = nome;
	}

}