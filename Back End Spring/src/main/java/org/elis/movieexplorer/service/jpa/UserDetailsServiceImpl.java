package org.elis.movieexplorer.service.jpa;

import lombok.RequiredArgsConstructor;
import org.elis.movieexplorer.repository.UtenteRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "service.impl", havingValue = "JPA")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UtenteRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findUtenteByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("utente non trovato per username: " + username));
	}
	
}
