package com.br.ecommerce.auth.service;

import com.br.ecommerce.auth.enums.Role;
import com.br.ecommerce.auth.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    private static final String SEGREDO_LONGO = "a".repeat(64);

    private final JwtService jwtService = new JwtService(SEGREDO_LONGO);

    @Test
    void deveGerarTokenDeUsuarioComIdNoSubjectERoleHs256() {
        Usuario usuario = Usuario.builder().id(42L).email("user@teste.com").role(Role.ROLE_ADMIN).build();

        Jws<Claims> jws = parse(jwtService.generateToken(usuario));

        assertEquals("HS256", jws.getHeader().getAlgorithm());
        assertEquals("42", jws.getBody().getSubject());
        assertEquals("user@teste.com", jws.getBody().get("email"));
        assertEquals("ROLE_ADMIN", jws.getBody().get("role"));
    }

    @Test
    void deveGerarTokenDeServicoComRoleService() {
        Jws<Claims> jws = parse(jwtService.generateServiceToken("pedido"));

        assertEquals("HS256", jws.getHeader().getAlgorithm());
        assertEquals("pedido", jws.getBody().getSubject());
        assertEquals("ROLE_SERVICE", jws.getBody().get("role"));
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SEGREDO_LONGO.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);
    }
}
