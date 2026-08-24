package org.elis.movieexplorer.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;
	@SuppressWarnings("unused")
	private final HandlerExceptionResolver resolver;
	private final JwtUtils jwtUtilities;

	public AuthFilter(UserDetailsService userDetailsService,
						@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, JwtUtils jwtUtilities){
		this.userDetailsService = userDetailsService;
		this.resolver = resolver;
		this.jwtUtilities = jwtUtilities;
	}

	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
	        throws ServletException, IOException {
		try{
			// controlliamo che l'utente non sia già stato autenticato da un filtro precedente
			SecurityContext securityContext = SecurityContextHolder.getContext();
			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			boolean isAuthenticated = securityContext.getAuthentication() != null;
			// se è già stato autenticato: mando avanti la filter chain ed esco fuori dal metodo
			if(authHeader == null || isAuthenticated || !authHeader.startsWith("Bearer")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authHeader.substring(7);
			String username = jwtUtilities.getSubject(token);
			UserDetails utente = userDetailsService.loadUserByUsername(username);
			// classe che estende Authentication
			// costruttore prende utente [principal], null [credentials] e utente.getAuthorities() [authorities]
			// invece del nome completo della classe, potrei usare "var" come tipo della variabile
			var upat = new UsernamePasswordAuthenticationToken(
					utente,
					null,
					utente.getAuthorities());
			upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			securityContext.setAuthentication(upat);
		}catch (Exception e){
			resolver.resolveException(request, response, null, e);
			return;
		}

		filterChain.doFilter(request, response);
	}
	
}
