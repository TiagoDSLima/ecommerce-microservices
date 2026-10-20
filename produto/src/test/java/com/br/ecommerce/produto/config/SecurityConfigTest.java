package com.br.ecommerce.produto.config;

import com.br.ecommerce.produto.controller.ProdutoController;
import com.br.ecommerce.produto.service.ProdutoService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = "auth.secret-key=" + SecurityConfigTest.SEGREDO)
class SecurityConfigTest {

    static final String SEGREDO = "segredo-de-teste-com-mais-de-32-bytes!!";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void getDeveSerPublico() throws Exception {
        mockMvc.perform(get("/produto/buscaTodos"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSemTokenDeveRetornar401() throws Exception {
        mockMvc.perform(delete("/produto/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteComRoleUserDeveRetornar403() throws Exception {
        mockMvc.perform(delete("/produto/1").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComRoleAdminDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/produto/1").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void tokenRealDeAdminDeveSerAceito() throws Exception {
        mockMvc.perform(delete("/produto/1").header("Authorization", "Bearer " + token("ROLE_ADMIN", SEGREDO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void tokenRealDeUserDeveRetornar403() throws Exception {
        mockMvc.perform(delete("/produto/1").header("Authorization", "Bearer " + token("ROLE_USER", SEGREDO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenAssinadoComOutroSegredoDeveRetornar401() throws Exception {
        String outroSegredo = "outro-segredo-com-mais-de-32-bytes-tambem!!";
        mockMvc.perform(delete("/produto/1").header("Authorization", "Bearer " + token("ROLE_ADMIN", outroSegredo)))
                .andExpect(status().isUnauthorized());
    }

    private String token(String role, String segredo) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("1")
                .claim("role", role)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 60_000))
                .build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        jwt.sign(new MACSigner(segredo.getBytes(StandardCharsets.UTF_8)));
        return jwt.serialize();
    }
}
