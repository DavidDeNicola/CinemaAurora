package org.elis.movieexplorer.security;

import java.util.List;

import org.elis.movieexplorer.model.enums.Ruolo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class MySecurityConfig {
	private final AuthFilter filter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setExposedHeaders(List.of("Authorization"));
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity config) throws Exception {
		config.headers(t -> t.frameOptions(t2 -> t2.disable()));
		config.csrf(t -> t.disable());
		config.cors(t -> t.configurationSource(corsConfigurationSource()));
		config.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		config.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		config.authorizeHttpRequests(t -> {
			t.requestMatchers("/admin/**").hasRole(Ruolo.SUPERADMIN.getNome());
			t.requestMatchers("/staff/**").hasAnyRole(Ruolo.STAFF.getNome(), Ruolo.SUPERADMIN.getNome());
			t.requestMatchers("/cliente/**").hasRole(Ruolo.CLIENTE.getNome());
			t.requestMatchers("/user/**").hasAnyRole(Ruolo.CLIENTE.getNome(),Ruolo.STAFF.getNome(),Ruolo.SUPERADMIN.getNome());

			t.anyRequest().permitAll();	
		});
		return config.build();
	}
}