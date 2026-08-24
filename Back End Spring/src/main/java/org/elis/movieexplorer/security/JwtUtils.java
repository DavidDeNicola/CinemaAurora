package org.elis.movieexplorer.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.elis.movieexplorer.model.Utente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${JWT_KEY}")
    private String KEY;

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(KEY.getBytes());
    }

    public String createToken(Utente utente){
        // Data attuale in millisecondi
        long todayMillis = System.currentTimeMillis();
        // (1000L = 1s) (60 = 1m) (60 = 1h) (24 = 1d) (30 = 1 mese)
        long expDateMillis = todayMillis + (1000L*60*60*24*30);
        Date iat = new Date(todayMillis);
        Date exp = new Date(expDateMillis);
        JwtBuilder builder = Jwts.builder();
        return builder
                .subject(utente.getEmail())
                .issuer("Movie Explorer")
                .issuedAt(iat)
                .expiration(exp)
                .claims()
                .add("ruolo", utente.getRuolo().toString())
                .and()
                .signWith(secretKey())
                .compact();
    }

    public String getSubject(String token){
        JwtParserBuilder builder = Jwts.parser();
        JwtParser parser = builder.verifyWith(secretKey()).build();
        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
