package br.ufpr.bantads.ms_auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.io.Decoders; 
import org.springframework.beans.factory.annotation.Value; // For reading from properties
import org.springframework.stereotype.Service;


@Service
public class JwtService {
    @Value("${key.jwt.secret}") // Lê a chave secreta do application.properties
    private String secretString;
    
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Inicializa a chave secreta após a construção do bean
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateToken(String login, String tipo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tipo", tipo); // Adiciona o tipo de usuário como uma claims personalizada

        return Jwts.builder()
                .setClaims(claims) // Adiciona as claims personalizadas
                .setSubject(login) // Define o main subject do token como o login do usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Define a data de expiração do token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta
                .compact(); // Gera o token JWT no formato compacto header.payload.signature
    }
}

