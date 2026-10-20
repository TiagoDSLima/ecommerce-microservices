package com.br.ecommerce.auth.service;

import com.br.ecommerce.auth.enums.Role;
import com.br.ecommerce.auth.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRACAO_USUARIO_MS = 1000 * 60 * 60;
    private static final long EXPIRACAO_SERVICO_MS = 1000 * 60 * 5;

    private final SecretKey key;

    public JwtService(@Value("${auth.secret-key}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .setSubject(String.valueOf(usuario.getId()))
                .claim("email", usuario.getEmail())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO_USUARIO_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateServiceToken(String clientId) {

        return Jwts.builder()
                .setSubject(clientId)
                .claim("role", Role.ROLE_SERVICE.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO_SERVICO_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getExpiracaoServicoSegundos() {
        return EXPIRACAO_SERVICO_MS / 1000;
    }
}
